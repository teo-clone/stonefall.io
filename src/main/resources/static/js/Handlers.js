let interface = {
  mousedown: false,
  leftdown: false,
  updown: false,
  rightdown: false,
  downdown: false,
  shiftdown: false,
  lastMousePos: {
    x: 0,
    y: 0
  }
};

const Handlers = function() {
  /**
   * Finds the coordinate of the box the pixel (x, y) is in.
   * @param {Number} x
   * @param {Number} y
   */
  const coordinateOf = (x, y) => {
    const boundingBoxCoordinates = game.boundingBox.getCoordinates();
    const canvas = singletonCanvas.getCanvas();
    return {
      x: Math.floor(
        boundingBoxCoordinates.topLeft.x +
          x *
            (boundingBoxCoordinates.bottomRight.x -
              boundingBoxCoordinates.topLeft.x) /
            canvas.width
      ),
      y: Math.floor(
        boundingBoxCoordinates.topLeft.y +
          y *
            (boundingBoxCoordinates.bottomRight.y -
              boundingBoxCoordinates.topLeft.y) /
            canvas.height
      )
    };
  };

  const moveChecker = (attacker, coordinates) => {
    if (attacker.x === coordinates.x && attacker.y === coordinates.y) {
      return true;
    } else if (attacker.movement && attacker.movement.ratio > 0) {
      switch (attacker.movement.direction) {
        case 0:
          return (
            attacker.x === coordinates.x && attacker.y - 1 === coordinates.y
          );
          break;
        case 1:
          return (
            attacker.x + 1 === coordinates.x && attacker.y - 1 === coordinates.y
          );
          break;
        case 2:
          return (
            attacker.x + 1 === coordinates.x && attacker.y === coordinates.y
          );
          break;
        case 3:
          return (
            attacker.x + 1 === coordinates.x && attacker.y + 1 === coordinates.y
          );
          break;
        case 4:
          return (
            attacker.x === coordinates.x && attacker.y + 1 === coordinates.y
          );
          break;
        case 5:
          return (
            attacker.x - 1 === coordinates.x && attacker.y + 1 === coordinates.y
          );
          break;
        case 6:
          return (
            attacker.x - 1 === coordinates.x && attacker.y === coordinates.y
          );
          break;
        case 7:
          return (
            attacker.x - 1 === coordinates.x && attacker.y - 1 === coordinates.y
          );
          break;
        default:
          return false;
          break;
      }
    }
  };

  const check = coordinates => {
    let myBobjects = game.getMyObjects();
    if(myBobjects.attackers){
      myBobjects.attackers.forEach(attacker => {
        if (moveChecker(attacker, coordinates)) {
          if (!interface.shiftdown) {
            game.transition("ESCAPE");
          }
          game.transition("SELECT_ATTACKERS", attacker);
        }
      });
    }
    if(myBobjects.walls){
      myBobjects.walls.forEach(wall => {
        if (coordinates.x === wall.x && coordinates.y === wall.y) {
          if (!interface.shiftdown) {
            game.transition("ESCAPE");
          }
          game.transition("SELECT_WALLS", wall);
        }
      });
    }
    if(myBobjects.turrets){
      myBobjects.turrets.forEach(turret => {
        if (coordinates.x === turret.x && coordinates.y === turret.y) {
          if (!interface.shiftdown) {
            game.transition("ESCAPE");
          }
          game.transition("SELECT_TURRETS", turret);
        }
      });
    }
    if(myBobjects.mines){
      myBobjects.mines.forEach(mine => {
        if (coordinates.x === mine.x && coordinates.y === mine.y) {
          if (!interface.shiftdown) {
            game.transition("ESCAPE");
          }
          game.transition("SELECT_MINES", mine);
        }
      });
    }
  };

  const mousedownHandler = e => {
    interface.mousedown = true;
    const x = e.pageX * singletonCanvas.getDpi();
    const y = e.pageY * singletonCanvas.getDpi();
    const coordinates = coordinateOf(x, y);
    switch (game.getCurrentState()) {
      case "idle":
        // check if the the current coordinate has an object
        check(coordinates);
        break;
      case "selectingWallSquare":
        game.transition("PLACE_WALL", coordinates);
        break;
      case "selectingTurretSquare":
        game.transition("PLACE_TURRET", coordinates);
        break;
      case "selectingAttackerSquare":
        game.transition("PLACE_ATTACKER", coordinates);
        break;
      case "selectingMineSquare":
        game.transition("PLACE_MINE", coordinates);
        break;
      case "selectingWalls":
        check(coordinates);
        break;
      case "selectingTurrets":
        check(coordinates);
        break;
      case "selectingAttackers":
        check(coordinates);
        break;
      case "selectingMines":
        check(coordinates);
        break;
    }
  };

  const mousemoveHandler = e => {
    if (interface.mousedown) {
      const delta = {
        x: e.pageX * singletonCanvas.getDpi() - interface.lastMousePos.x,
        y: e.pageY * singletonCanvas.getDpi() - interface.lastMousePos.y
      };
      game.boundingBox.updateFromDelta(delta);
    }
    interface.lastMousePos = {
      x: e.pageX * singletonCanvas.getDpi(),
      y: e.pageY * singletonCanvas.getDpi()
    };
  };

  const mouseupHandler = e => {
    interface.mousedown = false;
  };

  const rightClickHandler = e => {
    e.preventDefault();
    const x = e.pageX * singletonCanvas.getDpi();
    const y = e.pageY * singletonCanvas.getDpi();
    const coordinates = coordinateOf(x, y, game.boundingBox.getCoordinates());
    switch (game.getCurrentState()) {
      case "selectingAttackers":
        game.transition("PERFORM_ACTION", coordinates);
        break;
    }
  };

  const keydownHandler = e => {
    if (e.which === 70) {
      if (game.canBuyAttacker()) {
        game.transition("SELECT_ATTACKER_SQUARE");
      }
    } else if (e.which === 69) {
      if (game.canBuyWall()) {
        game.transition("SELECT_WALL_SQUARE");
      }
    } else if (e.which === 68) {
      if (game.canBuyTurret()) {
        game.transition("SELECT_TURRET_SQUARE");
      }
    } else if (e.which === 83) {
      if (game.canBuyMine()) {
        game.transition("SELECT_MINE_SQUARE");
      }
    } else if (e.keyCode === 27) {
      game.transition("ESCAPE");
    } else if (e.keyCode === 16) {
      interface.shiftdown = true;
    } else if (e.which === 37) {
      interface.leftdown = true;
    } else if (e.which === 38) {
      interface.updown = true;
    } else if (e.which === 39) {
      interface.rightdown = true;
    } else if (e.which === 40) {
      interface.downdown = true;
    } else if (e.which === 32) {
      game.centerOnBase();
    }
  };

  const keypressHandler = e => {};

  const keyupHandler = e => {
    if (e.which === 16) {
      interface.shiftdown = false;
    } else if (e.which === 37) {
      interface.leftdown = false;
    } else if (e.which === 38) {
      interface.updown = false;
    } else if (e.which === 39) {
      interface.rightdown = false;
    } else if (e.which === 40) {
      interface.downdown = false;
    }
  };

  const wallImageClickHandler = e => {
    if (game.canBuyWall()) {
      game.transition("SELECT_WALL_SQUARE");
    }
  };

  const turretImageClickHandler = e => {
    if (game.canBuyTurret()) {
      game.transition("SELECT_TURRET_SQUARE");
    }
  };

  const attackerImageClickHandler = e => {
    if (game.canBuyAttacker()) {
      game.transition("SELECT_ATTACKER_SQUARE");
    }
  };

  const mineImageClickHandler = e => {
    if (game.canBuyMine()) {
      game.transition("SELECT_MINE_SQUARE");
    }
  };

  const sellImageClickHandler = e => {
    game.transition("PERFORM_ACTION", "sell");
  };

  return {
    mousedownHandler,
    mousemoveHandler,
    mouseupHandler,
    rightClickHandler,
    keydownHandler,
    keypressHandler,
    keyupHandler,
    wallImageClickHandler,
    turretImageClickHandler,
    attackerImageClickHandler,
    mineImageClickHandler,
    sellImageClickHandler
  };
};
