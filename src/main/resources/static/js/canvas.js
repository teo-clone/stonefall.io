let singletonCanvas;
let penciller;

const drawGame = (
  boundingBox,
  objects,
  my,
  currentlySelectedObjects,
  animationFrame
) => {
  const boundingBoxCoordinates = boundingBox.getCoordinates();
  const canvas = singletonCanvas.getCanvas();
  const ctx = singletonCanvas.getCtx();
  penciller = Penciller();
  penciller.setCanvas(canvas);
  penciller.setCtx(ctx);
  penciller.setDpi(singletonCanvas.getDpi());
  penciller.setBoundingBoxCoordinates(boundingBoxCoordinates);

  /////// BACKGROUND ///////
  // draw the background
  penciller.pencilBackground();
  // draw the grid
  penciller.pencilGrid();
  //////////////////////////

  ////// GAME OBJECTS //////
  //draw game objects
  objects.players.forEach(player => {
    if(player && (player.id + 1)){
      let isMe = (player.id == game.getId());
      // draw my base
      if (player.walls) {
        // draw my walls
        player.walls.forEach(wall => {
          if (boundingBox.contains(wall)) {
            penciller.pencilWall(wall, player.color, isMe);
          }
        });
      }
      if (player.turrets) {
        // draw my turrets
        player.turrets.forEach(turret => {
          if (boundingBox.contains(turret)) {
            penciller.pencilTurret(turret, player.color, isMe);
          }
        });
      }
      if (player.attackers) {
        // draw my attackers
        player.attackers.forEach(attacker => {
          if (boundingBox.contains(attacker)) {
            penciller.pencilAttacker(attacker, player.color, animationFrame);
          }
        });
      }
      if (player.mines) {
        // draw my mines
        player.mines.forEach(mine => {
          if (boundingBox.contains(mine)) {
            penciller.pencilMine(mine, player.color, isMe);
          }
        });
      }
      if (player.scaffoldings) {
        // draw my scaffoldings
        player.scaffoldings.forEach(scaffolding => {
          if (boundingBox.contains(scaffolding)) {
            penciller.pencilScaffolding(scaffolding, player.color);
          }
        });
      }
      if (player.base) {
        if (boundingBox.contains(player.base)) {
          penciller.pencilBase(player.base, player.color, isMe);
        }
      }
    }
  });

  // draw resources
  let flag = false;
  objects.resources.forEach(resource => {
    if (boundingBox.contains(resource)) {
      penciller.pencilResource(resource);
    }
  });

  //////////////////////////

  /////////// GUI //////////
  switch (game.getCurrentState()) {
    // if the game is selecting objects, draw the selected objects
    case "selectingWalls":
      if(my.walls){
        my.walls.forEach(wall => {
          if (
            currentlySelectedObjects.map(object => object.id).includes(wall.id)
          ) {
            penciller.pencilSelectedWall(wall, my.color);
          }
        });
        penciller.pencilBuildingContextMenu(my.color);
      }
      break;
    case "selectingTurrets":
      if(my.turrets){
        my.turrets.forEach(turret => {
          if (
            currentlySelectedObjects.map(object => object.id).includes(turret.id)
          ) {
            penciller.pencilSelectedTurret(turret, my.color);
          }
        });
        penciller.pencilBuildingContextMenu(my.color);
      }
      break;
    case "selectingAttackers":
      if(my.attackers){
        my.attackers.forEach(attacker => {
          if (
            currentlySelectedObjects
              .map(object => object.id)
              .includes(attacker.id)
          ) {
            penciller.pencilSelectedAttacker(attacker, my.color, animationFrame);
          }
        });
      }
      break;
    case "selectingMines":
      if(my.mines){
        my.mines.forEach(mine => {
          if (
            currentlySelectedObjects.map(object => object.id).includes(mine.id)
          ) {
            penciller.pencilSelectedMine(mine, my.color);
          }
        });
        penciller.pencilBuildingContextMenu(my.color);
      }
      break;
    // if the game is selecting a square, draw the selection
    case "selectingWallSquare":
      const validSquares = highlightValidSquares(
        boundingBoxCoordinates,
        objects,
        my
      );
      const coordinates = penciller.coordinateOf(
        interface.lastMousePos.x,
        interface.lastMousePos.y
      );
      if (validSquares["i" + coordinates.x + "j" + coordinates.y]) {
        penciller.pencilWallSelection(interface.lastMousePos);
      }
      break;
    case "selectingTurretSquare":
      const validSquares2 = highlightValidSquares(
        boundingBoxCoordinates,
        objects,
        my
      );
      const coordinates2 = penciller.coordinateOf(
        interface.lastMousePos.x,
        interface.lastMousePos.y
      );
      if (validSquares2["i" + coordinates2.x + "j" + coordinates2.y]) {
        penciller.pencilTurretSelection(interface.lastMousePos);
      }
      break;
    case "selectingAttackerSquare":
      const validSquares3 = highlightValidSquares(
        boundingBoxCoordinates,
        objects,
        my
      );
      const coordinates3 = penciller.coordinateOf(
        interface.lastMousePos.x,
        interface.lastMousePos.y
      );
      if (validSquares3["i" + coordinates3.x + "j" + coordinates3.y]) {
        penciller.pencilAttackerSelection(interface.lastMousePos);
      }
      break;
    case "selectingMineSquare":
      const validSquares4 = highlightValidSquares(
        boundingBoxCoordinates,
        objects,
        my
      );
      const coordinates4 = penciller.coordinateOf(
        interface.lastMousePos.x,
        interface.lastMousePos.y
      );
      if (validSquares4["i" + coordinates4.x + "j" + coordinates4.y]) {
        penciller.pencilMineSelection(interface.lastMousePos);
      }
      break;
  }

  // draw the hud
  if (my && my.statistics) {
    penciller.pencilHud({
      ...my.statistics,
      color: my.color,
      leaderboard: objects.leaderboard
    });
  }
  //////////////////////////
};

const highlightValidSquares = (boundingBoxCoordinates, objects, my) => {
  // penciller.highlightValidSquares(boundingBoxCoordinates, objects);
  let validSquares = {};
  let x;
  let y;
  validSquares = { ...validSquares, ...validSquaresFrom(my.base) };
  if(my.walls){
    my.walls.forEach(wall => {
      const wallValidSquares = validSquaresFrom(wall);
      const wallValidSquaresEntries = Object.entries(wallValidSquares);
      wallValidSquaresEntries.forEach(entry => {
        validSquares[entry[0]] = entry[1];
      });
    });
  }
  if(my.turrets){
    my.turrets.forEach(turret => {
      const turretValidSquares = validSquaresFrom(turret);
      const turretValidSquaresEntries = Object.entries(turretValidSquares);
      turretValidSquaresEntries.forEach(entry => {
        validSquares[entry[0]] = entry[1];
      });
    });
  }
  if(my.mines){
    my.mines.forEach(mine => {
      const mineValidSquares = validSquaresFrom(mine);
      const mineValidSquaresEntries = Object.entries(mineValidSquares);
      mineValidSquaresEntries.forEach(entry => {
        validSquares[entry[0]] = entry[1];
      });
    });
  }

  singletonCanvas.getCtx().beginPath();
  for (
    let i = Math.floor(boundingBoxCoordinates.topLeft.x);
    i < boundingBoxCoordinates.bottomRight.x && i < game.BOARD_WIDTH;
    i++
  ) {
    if (i >= 0) {
      for (
        let j = Math.floor(boundingBoxCoordinates.topLeft.y);
        j < boundingBoxCoordinates.bottomRight.y && j < game.BOARD_HEIGHT;
        j++
      ) {
        if (j >= 0) {
          if (!validSquares["i" + i + "j" + j]) {
            penciller.pencilInvalidSquare(i, j);
          }
        }
      }
    }
  }
  singletonCanvas.getCtx().closePath();
  singletonCanvas.getCtx().fillStyle = "rgba(20, 20, 20, 0.2)";
  singletonCanvas.getCtx().fill();
  return validSquares;
}

const validSquaresFrom = object => {
  const newValidSquares = {};
  for (
    let i = object.x - game.MAX_BLOCK_DISTANCE;
    i <= object.x + game.MAX_BLOCK_DISTANCE;
    i++
  ) {
    for (
      let j = object.y - game.MAX_BLOCK_DISTANCE;
      j <= object.y + game.MAX_BLOCK_DISTANCE;
      j++
    ) {
      newValidSquares["i" + i + "j" + j] = true;
    }
  }
  return newValidSquares;
};

const initializeCanvas = () => {
  singletonCanvas = SingletonCanvas();
  singletonCanvas.setDpi(window.devicePixelRatio);
  singletonCanvas.setCanvas($("#game")[0]);
  return {
    width: singletonCanvas.getWidth(),
    height: singletonCanvas.getHeight()
  };
};
