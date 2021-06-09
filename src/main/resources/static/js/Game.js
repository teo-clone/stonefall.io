function Game(width, height, center) {
  const BOARD_WIDTH = 200;
  const BOARD_HEIGHT = 100;
  const MAX_BLOCK_DISTANCE = 3;
  const MAX_ANIMATION_FRAME = 30;
  let alertFlag = 1;
  let WALL_COST = 100;
  let TURRET_COST = 500;
  let ATTACKER_COST = 250;
  let MINE_COST = 1000;
  const BASE_MAX_HEALTH = 20000;
  let boundingBox = BoundingBox(width, height, center);
  let objects = {};
  let my;
  let id;
  let currentlySelectedObjects = [];
  let lastBaseHealth = -1;
  let animationFrame = 0;

  /**
   * Finds the pixel corresponding to the x and y coordinates on the grid.
   * @param {Number} x
   * @param {Number} y
   */
  const pixelOf = (x, y) => {
    const boundingBoxCoordinates = boundingBox.getCoordinates();
    const canvas = singletonCanvas.getCanvas();
    return {
      x:
        canvas.width /
        (boundingBoxCoordinates.bottomRight.x -
          boundingBoxCoordinates.topLeft.x) *
        (x - boundingBoxCoordinates.topLeft.x),
      y:
        canvas.height /
        (boundingBoxCoordinates.bottomRight.y -
          boundingBoxCoordinates.topLeft.y) *
        (y - boundingBoxCoordinates.topLeft.y)
    };
  };

  const update = () => {
    animationFrame = (animationFrame + 1) % MAX_ANIMATION_FRAME;
    const delta = {};
    if (interface.leftdown && !interface.rightdown) {
      delta.x = 12.5;
    } else if (interface.rightdown && !interface.leftdown) {
      delta.x = -12.5;
    } else {
      delta.x = 0;
    }
    if (interface.updown && !interface.downdown) {
      delta.y = 12.5;
    } else if (interface.downdown && !interface.updown) {
      delta.y = -12.5;
    } else {
      delta.y = 0;
    }
    boundingBox.updateFromDelta(delta);
    // update css stylings based on resources
    if (
      my &&
      my.statistics &&
      my.statistics.resources
    ) {
      
      //set basehealth to total base health
      if (lastBaseHealth == -1) lastBaseHealth = BASE_MAX_HEALTH;

      if(my.base.health){
        if (my.base.health < lastBaseHealth) {
          lastBaseHealth = my.base.health;
          alert("Your base is under attack!");
        }
      }
      const resources = my.statistics.resources;
      // update costs
      updateCosts(my.statistics);
      if (resources < WALL_COST) {
        $("#wallImage").css("background-color", "rgba(255, 0, 0, 0.3)");
        $("#wallImage").css("cursor", "not-allowed");
      } else {
        $("#wallImage").css("background-color", "rgba(20, 20, 20, 0.3)");
        $("#wallImage").css("cursor", "pointer");
      }
      if (resources < TURRET_COST) {
        $("#turretImage").css("background-color", "rgba(255, 0, 0, 0.3)");
        $("#turretImage").css("cursor", "not-allowed");
      } else {
        $("#turretImage").css("background-color", "rgba(20, 20, 20, 0.3)");
        $("#turretImage").css("cursor", "pointer");
      }
      if (resources < ATTACKER_COST) {
        $("#attacker1Image").css("background-color", "rgba(255, 0, 0, 0.3)");
        $("#attacker1Image").css("cursor", "not-allowed");
      } else {
        $("#attacker1Image").css("background-color", "rgba(20, 20, 20, 0.3)");
        $("#attacker1Image").css("cursor", "pointer");
      }
      if (resources < MINE_COST) {
        $("#mineImage").css("background-color", "rgba(255, 0, 0, 0.3)");
        $("#mineImage").css("cursor", "not-allowed");
      } else {
        $("#mineImage").css("background-color", "rgba(20, 20, 20, 0.3)");
        $("#mineImage").css("cursor", "pointer");
      }
    }
    if (my) {
      drawGame(boundingBox, objects, my, currentlySelectedObjects, animationFrame);
    }
  };

  // alert function
  const alert = message => {
    if (alertFlag) {
      alertFlag = 0;
      document.getElementById("alertBox").innerHTML = message;
      document.getElementById("alertBox").style.visibility = "visible";
      setTimeout(() => {
        document.getElementById("alertBox").style.visibility = "hidden";
        alertFlag = 1;
      }, 4000);
    }

    // $("#alertBox").show();
    // setTimeout(() => {
    //   $("#alertBox").hide();
    // }, 5000);
  };

  // update costs
  const updateCosts = statistics => {
    // set local costs of vars
    WALL_COST = my.statistics.wallCost;
    TURRET_COST = my.statistics.turret1Cost;
    ATTACKER_COST = my.statistics.attacker1Cost;
    MINE_COST = my.statistics.mineCost;
    // set costs of the p tags in game.ftl
    document.getElementById("wallCost").innerHTML = WALL_COST;
    if (WALL_COST >= 1000) {
      $("#wallCost").css("left", "calc(calc(calc(100vw / 2) - 82.5px) + 0px)");
    }
    document.getElementById("turret1Cost").innerHTML = TURRET_COST;
    if (TURRET_COST >= 1000) {
      $("#turret1Cost").css(
        "left",
        "calc(calc(calc(100vw / 2) - 27.5px) + 0px)"
      );
    }
    document.getElementById("attacker1Cost").innerHTML = ATTACKER_COST;
    if (ATTACKER_COST >= 1000) {
      $("#attackerCost").css(
        "left",
        "calc(calc(calc(100vw / 2) + 27.5px) + 0px)"
      );
    }
    document.getElementById("mineCost").innerHTML = MINE_COST;
  };

  const canBuyWall = () => my.statistics.resources >= WALL_COST;
  const canBuyTurret = () => my.statistics.resources >= TURRET_COST;
  const canBuyAttacker = () => my.statistics.resources >= ATTACKER_COST;
  const canBuyMine = () => my.statistics.resources >= MINE_COST;

  const getObjects = () => objects;

  const getMyObjects = () => my;

  const getId = () => id;

  const setObjects = newObjects => {
    objects = newObjects;
  };

  const setMyObjects = newMyObjects => {
    my = newMyObjects;
  };

  const setId = myId => {
    id = myId;
  };

  const placeWall = coordinates => {
    sock.sendWallSpawn(coordinates.x, coordinates.y);
  };

  const placeTurret = coordinates => {
    sock.sendTurretSpawn(coordinates.x, coordinates.y);
  };

  const placeAttacker = coordinates => {
    sock.sendAttackerSpawn(coordinates.x, coordinates.y);
  };

  const placeMine = coordinates => {
    sock.sendMineSpawn(coordinates.x, coordinates.y);
  };

  const selectWall = wall => {
    if (currentState !== "selectingWalls") {
      deselect();
    }
    currentlySelectedObjects.push(wall);
    $("#sellImage").css("display", "block");
  };

  const selectTurret = turret => {
    if (currentState !== "selectingTurrets") {
      deselect();
    }
    currentlySelectedObjects.push(turret);
    $("#sellImage").css("display", "block");
  };

  const selectAttacker = attacker => {
    if (currentState !== "selectingAttackers") {
      deselect();
    }
    currentlySelectedObjects.push(attacker);
  };

  const selectMine = mine => {
    if (currentState !== "selectingMines") {
      deselect();
    }
    currentlySelectedObjects.push(mine);
    $("#sellImage").css("display", "block");
  };

  const deselect = () => {
    currentlySelectedObjects = [];
    $("#sellImage").css("display", "none");
  };

  const selectSquareTransitions = {
    SELECT_WALL_SQUARE: {
      nextState: "selectingWallSquare"
    },
    SELECT_TURRET_SQUARE: {
      nextState: "selectingTurretSquare"
    },
    SELECT_ATTACKER_SQUARE: {
      nextState: "selectingAttackerSquare"
    },
    SELECT_MINE_SQUARE: {
      nextState: "selectingMineSquare"
    },
    ESCAPE: {
      nextState: "idle"
    }
  };

  const selectObjectTransitions = {
    SELECT_WALLS: {
      nextState: "selectingWalls",
      sideEffect: selectWall
    },
    SELECT_TURRETS: {
      nextState: "selectingTurrets",
      sideEffect: selectTurret
    },
    SELECT_ATTACKERS: {
      nextState: "selectingAttackers",
      sideEffect: selectAttacker
    },
    SELECT_MINES: {
      nextState: "selectingMines",
      sideEffect: selectMine
    }
  };

  const performWallAction = actionType => {
    if (actionType && actionType === "sell") {
      sock.sendWallsSell(currentlySelectedObjects);
      deselect();
    }
  };
  const performTurretAction = actionType => {
    if (actionType && actionType === "sell") {
      sock.sendTurretsSell(currentlySelectedObjects);
      deselect();
    }
  };
  const performAttackerAction = coordinates => {
    sock.sendAttack(currentlySelectedObjects, coordinates);
    deselect();
  };
  const performMineAction = actionType => {
    if (actionType && actionType === "sell") {
      sock.sendMinesSell(currentlySelectedObjects);
      deselect();
    }
  };

  const machine = {
    idle: {
      ...selectSquareTransitions,
      ...selectObjectTransitions
    },
    selectingWallSquare: {
      ...selectSquareTransitions,
      ESCAPE: {
        nextState: "idle"
      },
      PLACE_WALL: {
        nextState: "idle",
        sideEffect: placeWall
      }
    },
    selectingAttackerSquare: {
      ...selectSquareTransitions,
      PLACE_ATTACKER: {
        nextState: "idle",
        sideEffect: placeAttacker
      }
    },
    selectingTurretSquare: {
      ...selectSquareTransitions,
      PLACE_TURRET: {
        nextState: "idle",
        sideEffect: placeTurret
      }
    },
    selectingMineSquare: {
      ...selectSquareTransitions,
      PLACE_MINE: {
        nextState: "idle",
        sideEffect: placeMine
      }
    },
    selectingWalls: {
      ...selectSquareTransitions,
      ...selectObjectTransitions,
      ESCAPE: {
        nextState: "idle",
        sideEffect: deselect
      },
      PERFORM_ACTION: {
        nextState: "idle",
        sideEffect: performWallAction
      }
    },
    selectingTurrets: {
      ...selectSquareTransitions,
      ...selectObjectTransitions,
      ESCAPE: {
        nextState: "idle",
        sideEffect: deselect
      },
      PERFORM_ACTION: {
        nextState: "idle",
        sideEffect: performTurretAction
      }
    },
    selectingAttackers: {
      ...selectSquareTransitions,
      ...selectObjectTransitions,
      ESCAPE: {
        nextState: "idle",
        sideEffect: deselect
      },
      PERFORM_ACTION: {
        nextState: "idle",
        sideEffect: performAttackerAction
      }
    },
    selectingMines: {
      ...selectSquareTransitions,
      ...selectObjectTransitions,
      ESCAPE: {
        nextState: "idle",
        sideEffect: deselect
      },
      PERFORM_ACTION: {
        nextState: "idle",
        sideEffect: performMineAction
      }
    }
  };

  let currentState = "idle";
  const getCurrentState = () => currentState;

  // The finite state machine transition function
  const transition = (action, arguments) => {
    // perform any side effects of the transition
    if (machine[currentState][action]["sideEffect"]) {
      if (arguments) {
        machine[currentState][action]["sideEffect"](arguments);
      } else {
        machine[currentState][action]["sideEffect"]();
      }
    }
    currentState = machine[currentState][action]["nextState"];
  };

  const centerOnBase = () => {
    boundingBox.setCenter({ x: my.base.x, y: my.base.y });
  };

  const getAnimationFrame = () => {
    return animationFrame;
  };

  const getCoordinates = () => {
    return boundingBox.getCoordinates();
  }

  return {
    BOARD_HEIGHT,
    BOARD_WIDTH,
    MAX_BLOCK_DISTANCE,
    canBuyWall,
    canBuyTurret,
    canBuyAttacker,
    canBuyMine,
    boundingBox,
    getObjects,
    getMyObjects,
    getId,
    setObjects,
    setMyObjects,
    setId,
    getCurrentState,
    getCoordinates,
    transition,
    centerOnBase,
    getAnimationFrame,
    update
  };
}
