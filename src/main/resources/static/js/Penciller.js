const Penciller = function() {
  const MAX_HEALTH = {
    ATTACKER: 3000,
    MINE: 2000,
    TURRET: 500,
    WALL: 4000,
    BASE: 20000,
    RESOURCE: 5000,
  };

  let colorScheme = {
    background: "#80AF49",
    grid: "#5D7F35",
    hud: "rgba(80, 89, 104, 0.5)",
    outline: "#464c3f",
    darkOutline: "#1c1e19",
    white: "#ffffff",
    selectionOutline: "#a3fbff",
    text: "#000000",
    health: {
      bar: "#888888",
      remaining: "#ff0000"
    },
    sword: "#5e5a5a",
    my: {
      base: { block: "#000b87", heart: "#ff0000" },
      turret: {
        block: "#959adb",
        gunBase: "#464047",
        gun: "#111111",
        selection: {
          block: "#aaaaaa",
          gunBase: "#888888",
          gun: "#444444"
        }
      },
      attacker: {
        block: "#000b87",
        selection: {
          block: "#888888"
        }
      },
      mine: {
        block: "#434884",
        pickaxe: "#000000",
        selection: {
          block: "#888888",
          pickaxe: "#666666"
        }
      }
    },
    others: {
      base: { block: "#990000", heart: "#000000" },
      turret: { block: "#ce8a8a", gunBase: "#464047", gun: "#111111" },
      attacker: { block: "#990000" },
      mine: { block: "#4c1717" }
    },
    wall: {
      block: "#333333",
      selection: {
        block: "#999999"
      }
    },
    resource: {
      block: "#888888",
      speck: "#FFD700"
    }
  };

  let canvas;
  let ctx;
  let dpi;
  let boundingBoxCoordinates;

  const clip = (x, a, b) => {
    if (x < a) return a;
    if (x > b) return b;
    return x;
  };

  /**
   * Finds the pixel corresponding to the x and y coordinates on the grid.
   * @param {Number} x
   * @param {Number} y
   */
  const pixelOf = (x, y) => {
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

  /**
   * Finds the coordinate of the box the pixel (x, y) is in.
   * @param {Number} x
   * @param {Number} y
   */
  const coordinateOf = (x, y) => {
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

  /**
   * Draws a rounded rectangle using the current state of the canvas.
   * If you omit the last three params, it will draw a rectangle
   * outline with a 5 pixel border radius
   * @param {CanvasRenderingContext2D} ctx
   * @param {Number} x The top left x coordinate
   * @param {Number} y The top left y coordinate
   * @param {Number} width The width of the rectangle
   * @param {Number} height The height of the rectangle
   * @param {Number} [radius = 5] The corner radius; It can also be an object
   *                 to specify different radii for corners
   * @param {Number} [radius.tl = 0] Top left
   * @param {Number} [radius.tr = 0] Top right
   * @param {Number} [radius.br = 0] Bottom right
   * @param {Number} [radius.bl = 0] Bottom left
   */
  const roundRect = (x, y, width, height, radius) => {
    if (typeof radius === "number") {
      radius = { tl: radius, tr: radius, br: radius, bl: radius };
    } else {
      const defaultRadius = { tl: 0, tr: 0, br: 0, bl: 0 };
      for (let side in defaultRadius) {
        radius[side] = radius[side] || defaultRadius[side];
      }
    }
    ctx.moveTo(x + radius.tl, y);
    ctx.lineTo(x + width - radius.tr, y);
    ctx.quadraticCurveTo(x + width, y, x + width, y + radius.tr);
    ctx.lineTo(x + width, y + height - radius.br);
    ctx.quadraticCurveTo(
      x + width,
      y + height,
      x + width - radius.br,
      y + height
    );
    ctx.lineTo(x + radius.bl, y + height);
    ctx.quadraticCurveTo(x, y + height, x, y + height - radius.bl);
    ctx.lineTo(x, y + radius.tl);
    ctx.quadraticCurveTo(x, y, x + radius.tl, y);
  };

  const coordinateRect = (x, y, width, height) => {
    const topLeftPixel = pixelOf(
      clip(x, 0, game.BOARD_WIDTH),
      clip(y, 0, game.BOARD_HEIGHT)
    );
    const bottomRightPixel = pixelOf(
      clip(x + width, 0, game.BOARD_WIDTH),
      clip(y + height, 0, game.BOARD_HEIGHT)
    );
    ctx.rect(
      topLeftPixel.x,
      topLeftPixel.y,
      bottomRightPixel.x - topLeftPixel.x,
      bottomRightPixel.y - topLeftPixel.y
    );
  };

  const coordinateBlock = (x, y) => {
    const topLeftPixel = pixelOf(x + 0.15, y + 0.15);
    const bottomRightPixel = pixelOf(x + 0.85, y + 0.85);
    ctx.rect(
      topLeftPixel.x,
      topLeftPixel.y,
      bottomRightPixel.x - topLeftPixel.x,
      bottomRightPixel.y - topLeftPixel.y
    );
  };

  const coordinateHeart = (coordinateX, coordinateY) => {
    const pixelValues = pixelOf(coordinateX + 0.25, coordinateY + 0.25);
    const x = pixelValues.x;
    const y = pixelValues.y;
    const d = pixelOf(coordinateX + 0.75, coordinateY + 0.85).x - x;
    ctx.moveTo(x, y + d / 4);
    ctx.quadraticCurveTo(x, y, x + d / 4, y);
    ctx.quadraticCurveTo(x + d / 2, y, x + d / 2, y + d / 4);
    ctx.quadraticCurveTo(x + d / 2, y, x + d * 3 / 4, y);
    ctx.quadraticCurveTo(x + d, y, x + d, y + d / 4);
    ctx.quadraticCurveTo(x + d, y + d / 2, x + d * 3 / 4, y + d * 3 / 4);
    ctx.lineTo(x + d / 2, y + d);
    ctx.lineTo(x + d / 4, y + d * 3 / 4);
    ctx.quadraticCurveTo(x, y + d / 2, x, y + d / 4);
  };

  const calculateOffset = (direction, ratio) => {
    if (!ratio) {
      ratio = 0;
    }
    const animationWorkaroundRatio = ratio + 0.09;
    let movementCoordinateOffset = {
      x: 0,
      y: 0
    };
    if (typeof direction === "number") {
      switch (direction) {
        case 0:
          movementCoordinateOffset = {
            x: 0,
            y: -1 * ratio
          };
          break;
        case 1:
          movementCoordinateOffset = {
            x: animationWorkaroundRatio,
            y: -1 * animationWorkaroundRatio
          };
          break;
        case 2:
          movementCoordinateOffset = {
            x: ratio,
            y: 0
          };
          break;
        case 3:
          movementCoordinateOffset = {
            x: animationWorkaroundRatio,
            y: animationWorkaroundRatio
          };
          break;
        case 4:
          movementCoordinateOffset = {
            x: 0,
            y: ratio
          };
          break;
        case 5:
          movementCoordinateOffset = {
            x: -1 * animationWorkaroundRatio,
            y: animationWorkaroundRatio
          };
          break;
        case 6:
          movementCoordinateOffset = {
            x: -ratio,
            y: 0
          };
          break;
        case 7:
          movementCoordinateOffset = {
            x: -1 * animationWorkaroundRatio,
            y: -1 * animationWorkaroundRatio
          };
          break;
      }
    }
    return movementCoordinateOffset;
  };

  const coordinateTriangle = (x, y, direction, ratio) => {
    const movementCoordinateOffset = calculateOffset(direction, ratio);
    const bottomRightPixel = pixelOf(
      x + 0.85 + movementCoordinateOffset.x,
      y + 0.85 + movementCoordinateOffset.y
    );
    const topPixel = pixelOf(
      x + 0.5 + movementCoordinateOffset.x,
      y + 0.15 + movementCoordinateOffset.y
    );
    const bottomLeftPixel = pixelOf(
      x + 0.15 + movementCoordinateOffset.x,
      y + 0.85 + movementCoordinateOffset.y
    );
    const centerPixel = pixelOf(
      x + 0.5 + movementCoordinateOffset.x,
      y + 0.5 + movementCoordinateOffset.y
    );
    ctx.translate(centerPixel.x, centerPixel.y);
    if (typeof direction === "number") {
      ctx.rotate(direction * Math.PI / 4.0);
    }
    ctx.moveTo(topPixel.x - centerPixel.x, topPixel.y - centerPixel.y);
    ctx.lineTo(
      bottomRightPixel.x - centerPixel.x,
      bottomRightPixel.y - centerPixel.y
    );
    ctx.lineTo(
      bottomLeftPixel.x - centerPixel.x,
      bottomLeftPixel.y - centerPixel.y
    );
    ctx.lineTo(topPixel.x - centerPixel.x, topPixel.y - centerPixel.y);
  };

  const coordinateSword = (x, y, direction, ratio, animationFrame) => {
    const movementCoordinateOffset = calculateOffset(direction, ratio);
    const centerPixel = pixelOf(
      x + 0.5 + movementCoordinateOffset.x,
      y + 0.5 + movementCoordinateOffset.y
    );
    let animationSwordOffset;
    const halfAnimationFrame = 15;
    const swordDistance = 0.4;
    if (animationFrame < halfAnimationFrame) {
      animationSwordOffset =
        animationFrame / halfAnimationFrame * swordDistance;
    } else {
      animationSwordOffset =
        swordDistance -
        (animationFrame - halfAnimationFrame) /
          halfAnimationFrame *
          swordDistance;
    }
    const bottomSwordPixel = pixelOf(
      x + 0.8 + movementCoordinateOffset.x,
      y + 0.6 + movementCoordinateOffset.y - animationSwordOffset
    );
    const topSwordPixel = pixelOf(
      x + 0.8 + movementCoordinateOffset.x,
      y + 0.2 + movementCoordinateOffset.y - animationSwordOffset
    );
    const leftHiltPixel = pixelOf(
      x + 0.75 + movementCoordinateOffset.x,
      y + 0.5 + movementCoordinateOffset.y - animationSwordOffset
    );
    const rightHiltPixel = pixelOf(
      x + 0.85 + movementCoordinateOffset.x,
      y + 0.5 + movementCoordinateOffset.y - animationSwordOffset
    );
    ctx.translate(centerPixel.x, centerPixel.y);
    if (typeof direction === "number") {
      ctx.rotate(direction * Math.PI / 4.0);
    }
    ctx.moveTo(
      bottomSwordPixel.x - centerPixel.x,
      bottomSwordPixel.y - centerPixel.y
    );
    ctx.lineTo(
      topSwordPixel.x - centerPixel.x,
      topSwordPixel.y - centerPixel.y
    );
    ctx.moveTo(
      leftHiltPixel.x - centerPixel.x,
      leftHiltPixel.y - centerPixel.y
    );
    ctx.lineTo(
      rightHiltPixel.x - centerPixel.x,
      rightHiltPixel.y - centerPixel.y
    );
  };

  const coordinateSilo = (x, y, direction, ratio) => {
    const movementCoordinateOffset = calculateOffset(direction, ratio);
    const bottomRightPixel = pixelOf(
      x + 0.85 + movementCoordinateOffset.x,
      y + 0.85 + movementCoordinateOffset.y
    );
    const topRightPixel = pixelOf(
      x + 0.85 + movementCoordinateOffset.x,
      y + 0.5 + movementCoordinateOffset.y
    );
    const bottomLeftPixel = pixelOf(
      x + 0.15 + movementCoordinateOffset.x,
      y + 0.85 + movementCoordinateOffset.y
    );
    const topLeftPixel = pixelOf(
      x + 0.15 + movementCoordinateOffset.x,
      y + 0.5 + movementCoordinateOffset.y
    );
    const topBoxPixel = pixelOf(
      x + 0.5 + movementCoordinateOffset.x,
      y + 0.5 + movementCoordinateOffset.y
    );
    const topArcPixel = pixelOf(
      x + 0.5 + movementCoordinateOffset.x,
      y + 0.15 + movementCoordinateOffset.y
    );
    const centerPixel = pixelOf(
      x + 0.5 + movementCoordinateOffset.x,
      y + 0.5 + movementCoordinateOffset.y
    );
    ctx.translate(centerPixel.x, centerPixel.y);
    if (typeof direction === "number") {
      ctx.rotate(direction * Math.PI / 4.0);
    }
    ctx.moveTo(
      bottomLeftPixel.x - centerPixel.x,
      bottomLeftPixel.y - centerPixel.y
    );
    ctx.lineTo(topLeftPixel.x - centerPixel.x, topLeftPixel.y - centerPixel.y);
    ctx.arc(
      topBoxPixel.x - centerPixel.x,
      topBoxPixel.y - centerPixel.y,
      topBoxPixel.y - topArcPixel.y,
      4 * Math.PI / 4,
      0 * Math.PI / 4,
      false
    );
    ctx.lineTo(
      topRightPixel.x - centerPixel.x,
      topRightPixel.y - centerPixel.y
    );
    ctx.lineTo(
      bottomRightPixel.x - centerPixel.x,
      bottomRightPixel.y - centerPixel.y
    );
    ctx.lineTo(
      bottomLeftPixel.x - centerPixel.x,
      bottomLeftPixel.y - centerPixel.y
    );
  };

  const getColorScheme = () => {
    return colorScheme;
  };
  const setColorScheme = newColorScheme => {
    colorScheme = newColorScheme;
  };
  const getCanvas = () => {
    return canvas;
  };
  const setCanvas = newCanvas => {
    canvas = newCanvas;
  };
  const getCtx = () => {
    return ctx;
  };
  const setCtx = newCtx => {
    ctx = newCtx;
  };
  const getDpi = () => {
    return dpi;
  };
  const setDpi = newDpi => {
    dpi = newDpi;
  };
  const getBoundingBoxCoordinates = () => {
    return boundingBoxCoordinates;
  };
  const setBoundingBoxCoordinates = newBoundingBoxCoordinates => {
    boundingBoxCoordinates = newBoundingBoxCoordinates;
  };

  const pencilBackground = () => {
    ctx.beginPath();
    ctx.fillStyle = colorScheme.background;
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    ctx.closePath();
    ctx.fill();
  };

  const pencilGrid = () => {
    ctx.beginPath();
    // finding all the integers between boundingBoxCoordinates.topLeft.x
    // and boundingBoxCoordinates.bottomRight.x
    for (
      let i = Math.ceil(boundingBoxCoordinates.topLeft.x);
      i < boundingBoxCoordinates.bottomRight.x && i <= game.BOARD_WIDTH;
      i++
    ) {
      if (i >= 0) {
        // draw a line from (i, 0) to (i, game.BOARD_HEIGHT)
        const topPixel = pixelOf(i, 0);
        const bottomPixel = pixelOf(i, game.BOARD_HEIGHT);
        ctx.moveTo(topPixel.x, topPixel.y);
        ctx.lineTo(bottomPixel.x, bottomPixel.y);
      }
    }

    for (
      let j = Math.ceil(boundingBoxCoordinates.topLeft.y);
      j < boundingBoxCoordinates.bottomRight.y && j <= game.BOARD_HEIGHT;
      j++
    ) {
      if (j >= 0) {
        // draw a line from (0, j) to (game.BOARD_WIDTH, j)
        const leftPixel = pixelOf(0, j);
        const rightPixel = pixelOf(game.BOARD_WIDTH, j);
        ctx.moveTo(leftPixel.x, leftPixel.y);
        ctx.lineTo(rightPixel.x, rightPixel.y);
      }
    }
    ctx.closePath();
    ctx.strokeStyle = colorScheme.grid;
    ctx.stroke();
  };

  const pencilBase = (base, color, belongsToMe) => {
    ctx.beginPath();
    coordinateBlock(base.x, base.y);
    ctx.closePath();
    ctx.strokeStyle = colorScheme.darkOutline;
    ctx.lineWidth = 3;
    ctx.stroke();
    ctx.lineWidth = 1;
    ctx.fillStyle = color;
    ctx.fill();
    //draw heart if my base
    ctx.beginPath();
    coordinateHeart(base.x, base.y);
    ctx.closePath();
    ctx.strokeStyle = colorScheme.darkOutline;
    ctx.stroke();
    if (belongsToMe) {
      ctx.fillStyle = colorScheme.health.remaining;
    } else {
      ctx.fillStyle = colorScheme.darkOutline;
    }
    ctx.fill();
    pencilHealth(base, MAX_HEALTH.BASE);
    ctx.textAlign = "center";
    const textPixel = pixelOf(base.x + 0.5, base.y + 1.6);
    ctx.fillStyle = colorScheme.white;
    ctx.font = "bold 15px lato";
    ctx.fillText(base.name, textPixel.x, textPixel.y);
    ctx.textAlign = "left";
  };

  const pencilWall = (wall, color) => {
    ctx.beginPath();
    coordinateBlock(wall.x, wall.y);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.lineWidth = 2;
    ctx.strokeStyle = colorScheme.darkOutline;
    ctx.stroke();
    // draw the middle lines
    const topLeft = pixelOf(wall.x + 0.15, wall.y + 0.15);
    const topRight = pixelOf(wall.x + 0.85, wall.y + 0.15);
    const bottomLeft = pixelOf(wall.x + 0.15, wall.y + 0.85);
    const bottomRight = pixelOf(wall.x + 0.85, wall.y + 0.85);
    ctx.beginPath();
    ctx.moveTo(topLeft.x, topLeft.y);
    ctx.lineTo(bottomRight.x, bottomRight.y);
    ctx.moveTo(topRight.x, topRight.y);
    ctx.lineTo(bottomLeft.x, bottomLeft.y);
    ctx.closePath();
    ctx.lineWidth = 2;
    ctx.stroke();
    ctx.lineWidth = 1;
    pencilHealth(wall, MAX_HEALTH.WALL);
  };

  const pencilSelectedWall = (wall, color) => {
    // draw the middle lines
    ctx.beginPath();
    coordinateBlock(wall.x, wall.y);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = colorScheme.darkOutline;
    const topLeft = pixelOf(wall.x + 0.15, wall.y + 0.15);
    const topRight = pixelOf(wall.x + 0.85, wall.y + 0.15);
    const bottomLeft = pixelOf(wall.x + 0.15, wall.y + 0.85);
    const bottomRight = pixelOf(wall.x + 0.85, wall.y + 0.85);
    ctx.beginPath();
    ctx.moveTo(topLeft.x, topLeft.y);
    ctx.lineTo(bottomRight.x, bottomRight.y);
    ctx.moveTo(topRight.x, topRight.y);
    ctx.lineTo(bottomLeft.x, bottomLeft.y);
    ctx.closePath();
    ctx.lineWidth = 2;
    ctx.stroke();
    ctx.beginPath();
    coordinateBlock(wall.x, wall.y);
    ctx.closePath();
    ctx.strokeStyle = colorScheme.selectionOutline;
    ctx.stroke();
    ctx.lineWidth = 1;
    pencilHealth(wall, MAX_HEALTH.WALL);
  };

  const pencilTurret = (turret, color, belongsToMe) => {
    // draw the block
    ctx.beginPath();
    coordinateBlock(turret.x, turret.y, 1, 1);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.lineWidth = 2;
    ctx.strokeStyle = colorScheme.darkOutline;
    ctx.stroke();
    ctx.lineWidth = 1;

    // draw the gun base
    ctx.beginPath();
    const centerPixel = pixelOf(turret.x + 0.5, turret.y + 0.5);
    const topPixel = pixelOf(turret.x + 0.5, turret.y + 0.225);
    const topOfGunPixel = pixelOf(turret.x + 0.5, turret.y + 0.4);
    ctx.arc(
      centerPixel.x,
      centerPixel.y,
      centerPixel.y - topPixel.y,
      0,
      2 * Math.PI
    );
    ctx.closePath();
    if (belongsToMe) {
      ctx.fillStyle = colorScheme.my.turret.gunBase;
    } else {
      ctx.fillStyle = colorScheme.others.turret.gunBase;
    }
    ctx.fill();

    // draw the gun
    ctx.beginPath();
    ctx.arc(
      centerPixel.x,
      centerPixel.y,
      centerPixel.y - topOfGunPixel.y,
      0,
      2 * Math.PI
    );
    ctx.closePath();
    if (belongsToMe) {
      ctx.fillStyle = colorScheme.my.turret.gun;
    } else {
      ctx.fillStyle = colorScheme.others.turret.gun;
    }
    ctx.fill();

    // if the turret is attacking something
    if (turret.attacking) {
      // draw a line from the center of the turret to the x, y value
      const offset = calculateOffset(
        turret.attacking.direction,
        turret.attacking.ratio
      );
      const attackingPixel = pixelOf(
        turret.attacking.x + 0.5 + offset.x,
        turret.attacking.y + 0.5 + offset.y
      );
      ctx.beginPath();
      ctx.moveTo(centerPixel.x, centerPixel.y);
      ctx.lineTo(attackingPixel.x, attackingPixel.y);
      ctx.closePath();
      ctx.strokeStyle = colorScheme.health.remaining;
      ctx.lineWidth = 1;
      ctx.stroke();
    }
    pencilHealth(turret, MAX_HEALTH.TURRET);
  };

  const pencilSelectedTurret = (turret, color, belongsToMe) => {
    // draw the block
    ctx.beginPath();
    coordinateBlock(turret.x, turret.y);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();

    // draw the gun base
    ctx.beginPath();
    const centerPixel = pixelOf(turret.x + 0.5, turret.y + 0.5);
    const topPixel = pixelOf(turret.x + 0.5, turret.y + 0.225);
    const topOfGunPixel = pixelOf(turret.x + 0.5, turret.y + 0.4);
    ctx.arc(
      centerPixel.x,
      centerPixel.y,
      centerPixel.y - topPixel.y,
      0,
      2 * Math.PI
    );
    ctx.closePath();
    if (belongsToMe) {
      ctx.fillStyle = colorScheme.my.turret.gunBase;
    } else {
      ctx.fillStyle = colorScheme.others.turret.gunBase;
    }
    ctx.fill();

    // draw the gun
    ctx.beginPath();
    ctx.arc(
      centerPixel.x,
      centerPixel.y,
      centerPixel.y - topOfGunPixel.y,
      0,
      2 * Math.PI
    );
    ctx.closePath();
    if (belongsToMe) {
      ctx.fillStyle = colorScheme.my.turret.gun;
    } else {
      ctx.fillStyle = colorScheme.others.turret.gun;
    }
    ctx.fill();

    ctx.beginPath();
    coordinateBlock(turret.x, turret.y);
    ctx.closePath();
    ctx.strokeStyle = colorScheme.selectionOutline;
    ctx.lineWidth = 2;
    ctx.stroke();
    ctx.lineWidth = 1;
    pencilHealth(turret, MAX_HEALTH.TURRET);
  };

  const pencilAttacker = (attacker, color, animationFrame) => {
    ctx.beginPath();
    if (attacker.movement) {
      coordinateTriangle(
        attacker.x,
        attacker.y,
        attacker.movement.direction,
        attacker.movement.ratio
      );
    } else {
      coordinateTriangle(attacker.x, attacker.y);
    }

    ctx.closePath();
    ctx.strokeStyle = colorScheme.outline;
    ctx.stroke();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = colorScheme.darkOutline;
    ctx.lineWidth = 2;
    ctx.stroke();
    ctx.lineWidth = 1;
    ctx.resetTransform();

    if (attacker.isAttacking) {
      ctx.beginPath();
      coordinateSword(
        attacker.x,
        attacker.y,
        attacker.movement.direction,
        attacker.movement.ratio,
        animationFrame
      );
      ctx.closePath();
      ctx.strokeStyle = colorScheme.sword;
      ctx.lineWidth = 1;
      ctx.stroke();
      ctx.resetTransform();
    }

    pencilAttackerHealth(attacker, MAX_HEALTH.ATTACKER);
  };

  const pencilSelectedAttacker = (attacker, color, animationFrame) => {
    ctx.beginPath();
    if (attacker.movement) {
      coordinateTriangle(
        attacker.x,
        attacker.y,
        attacker.movement.direction,
        attacker.movement.ratio
      );
    } else {
      coordinateTriangle(attacker.x, attacker.y);
    }
    ctx.closePath();
    ctx.strokeStyle = colorScheme.outline;
    ctx.stroke();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = colorScheme.selectionOutline;
    ctx.lineWidth = 2;
    ctx.stroke();
    ctx.lineWidth = 1;
    ctx.resetTransform();

    if (attacker.isAttacking) {
      ctx.beginPath();
      coordinateSword(
        attacker.x,
        attacker.y,
        attacker.movement.direction,
        attacker.movement.ratio,
        animationFrame
      );
      ctx.closePath();
      ctx.strokeStyle = colorScheme.sword;
      ctx.lineWidth = 1;
      ctx.stroke();
      ctx.resetTransform();
    }

    pencilAttackerHealth(attacker, MAX_HEALTH.ATTACKER);
  };

  const pencilMine = (mine, color) => {
    // draw the block
    ctx.beginPath();
    coordinateBlock(mine.x, mine.y);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = colorScheme.darkOutline;
    ctx.lineWidth = 2;
    ctx.stroke();

    // draw the pickaxe arc
    ctx.beginPath();
    const centerPixel = pixelOf(mine.x + 0.5, mine.y + 0.5);
    const topPixel = pixelOf(mine.x + 0.5, mine.y + 0.3);
    ctx.arc(
      centerPixel.x,
      centerPixel.y,
      centerPixel.y - topPixel.y,
      -4.5 * Math.PI / 4,
      -1.5 * Math.PI / 4
    );
    ctx.strokeStyle = colorScheme.my.mine.pickaxe;
    ctx.lineWidth = 2;
    ctx.stroke();
    // draw the pickaxe handle
    const topLeftPixel = pixelOf(mine.x + 0.29, mine.y + 0.29);
    const bottomRightPixel = pixelOf(mine.x + 0.7, mine.y + 0.7);
    ctx.moveTo(topLeftPixel.x, topLeftPixel.y);
    ctx.lineTo(bottomRightPixel.x, bottomRightPixel.y);
    ctx.closePath();
    ctx.stroke();
    ctx.lineWidth = 1;
    pencilHealth(mine, MAX_HEALTH.MINE);
  };

  const pencilSelectedMine = (mine, color) => {
    // draw the block
    ctx.beginPath();
    coordinateBlock(mine.x, mine.y, 1, 1);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = colorScheme.selectionOutline;
    ctx.lineWidth = 2;
    ctx.stroke();
    ctx.lineWidth = 1;

    // draw the pickaxe arc
    ctx.beginPath();
    const centerPixel = pixelOf(mine.x + 0.5, mine.y + 0.5);
    const topPixel = pixelOf(mine.x + 0.5, mine.y + 0.3);
    ctx.arc(
      centerPixel.x,
      centerPixel.y,
      centerPixel.y - topPixel.y,
      -4.5 * Math.PI / 4,
      -1.5 * Math.PI / 4
    );
    ctx.strokeStyle = colorScheme.my.mine.pickaxe;
    ctx.lineWidth = 2;
    ctx.stroke();
    // draw the pickaxe handle
    const topLeftPixel = pixelOf(mine.x + 0.29, mine.y + 0.29);
    const bottomRightPixel = pixelOf(mine.x + 0.7, mine.y + 0.7);
    ctx.moveTo(topLeftPixel.x, topLeftPixel.y);
    ctx.lineTo(bottomRightPixel.x, bottomRightPixel.y);
    ctx.closePath();
    ctx.stroke();
    ctx.lineWidth = 1;
    coordinateRect(mine.x, mine.y, 1, 1);
    pencilHealth(mine, MAX_HEALTH.MINE);
  };

  const pencilResource = resource => {
    ctx.beginPath();
    const pixel1 = pixelOf(resource.x + 0.2, resource.y + 0.55);
    const pixel2 = pixelOf(resource.x + 0.25, resource.y + 0.85);
    const pixel3 = pixelOf(resource.x + 0.8, resource.y + 0.85);
    const pixel4 = pixelOf(resource.x + 0.85, resource.y + 0.45);
    const pixel5 = pixelOf(resource.x + 0.5, resource.y + 0.15);
    ctx.moveTo(pixel1.x, pixel1.y);
    ctx.lineTo(pixel2.x, pixel2.y);
    ctx.lineTo(pixel3.x, pixel3.y);
    ctx.lineTo(pixel4.x, pixel4.y);
    ctx.lineTo(pixel5.x, pixel5.y);
    ctx.lineTo(pixel1.x, pixel1.y);
    ctx.closePath();
    ctx.fillStyle = colorScheme.resource.block;
    ctx.fill();
    ctx.strokeStyle = colorScheme.darkOutline;
    ctx.lineWidth = 2;
    ctx.stroke();
    ctx.lineWidth = 1;

    // draw the gold dots
    ctx.beginPath();
    coordinateRect(resource.x + 0.3, resource.y + 0.6, 0.1, 0.1);
    coordinateRect(resource.x + 0.5, resource.y + 0.3, 0.1, 0.1);
    coordinateRect(resource.x + 0.5, resource.y + 0.7, 0.1, 0.1);
    ctx.closePath();
    ctx.fillStyle = colorScheme.resource.speck;
    ctx.fill();
    pencilHealth(resource, MAX_HEALTH.RESOURCE);
  };

  const pencilHud = (hud, leaderboard) => {
    ctx.globalAlpha = 0.5;

    // top left corner hud
    ctx.beginPath();
    roundRect(5 * dpi, 5 * dpi, 135 * dpi, 60 * dpi, 5 * dpi);
    ctx.closePath();
    ctx.fillStyle = hud.color;
    ctx.fill();
    ctx.lineWidth = 2;
    ctx.strokeStyle = colorScheme.outline;
    ctx.stroke();
    ctx.font = 15 * dpi + "px lato";
    ctx.fillStyle = colorScheme.text;
    ctx.globalAlpha = 1.0;

    // ensure that player name doesn't go off the edge
    let myName = hud.name;
    let myNameLen = myName.length;
    if(myNameLen > 9){
      myName = myName.substring(0, 10) + "...";
    }
    ctx.fillText(myName, 10 * dpi, 20 * dpi);
    ctx.fillText("Resources: " + hud.resources, 10 * dpi, 40 * dpi);
    ctx.fillText("Score: " + hud.score, 10 * dpi, 60 * dpi);
    ctx.globalAlpha = 0.5;

    //top right corner hud (leaderboard)
    let xOffset = canvas.width - 165 * dpi;
    let yOffset = 5 * dpi;
    ctx.beginPath();
    roundRect(xOffset, yOffset, 160 * dpi, 240 * dpi, 5 * dpi);
    ctx.closePath();
    ctx.fillStyle = colorScheme.hud;
    ctx.fill();
    ctx.lineWidth = 2;
    ctx.strokeStyle = colorScheme.outline;
    ctx.stroke();
    ctx.font = 15 * dpi + "px lato";
    ctx.fillStyle = colorScheme.text;
    ctx.globalAlpha = 1.0;

    ctx.fillText("Name", xOffset + 5 * dpi, 20 * dpi);
    ctx.fillText("Score", xOffset + 115 * dpi, 20 * dpi);

    //fill winning names
    let posMultiplier = 1;
    hud.leaderboard.forEach(player => {
      // set the place counter
      if (posMultiplier == 10) {
        ctx.fillText(
          posMultiplier + ".",
          xOffset - 3 * dpi,
          20 * dpi + posMultiplier * 20 * dpi
        );
      } else {
        ctx.fillText(
          posMultiplier + ".",
          xOffset + 5 * dpi,
          20 * dpi + posMultiplier * 20 * dpi
        );
      }
      ctx.fillStyle = player.color;

      // ensure that player name doesn't go off the edge
      let playerName = player.name;
      let nameLen = playerName.length;
      if(nameLen > 6){
        playerName = playerName.substring(0, 7) + "...";
      }
      ctx.fillText(
        playerName,
        xOffset + 20 * dpi,
        20 * dpi + posMultiplier * 20 * dpi
      );
      ctx.fillStyle = colorScheme.text;
      ctx.fillText(
        player.score,
        xOffset + 115 * dpi,
        20 * dpi + posMultiplier * 20 * dpi
      );
      posMultiplier = posMultiplier + 1;
    });
    // // ctx.fillText(hud.name, xOffset + 10*dpi, 20 * dpi);
    // ctx.fillText("Resources: " + hud.resources, xOffset + 10 * dpi, 40 * dpi);
    // ctx.fillText("Score: " + hud.score, xOffset + 10 * dpi, 60 * dpi);
    ctx.lineWidth = 1;
  };

  const pencilWallSelection = mousePos => {
    const coordinates = coordinateOf(
      interface.lastMousePos.x,
      interface.lastMousePos.y
    );
    if (
      coordinates.x >= 0 &&
      coordinates.y >= 0 &&
      coordinates.x < game.BOARD_WIDTH &&
      coordinates.y < game.BOARD_HEIGHT
    ) {
      ctx.beginPath();
      coordinateBlock(coordinates.x, coordinates.y, 1, 1);
      ctx.closePath();
      ctx.fillStyle = colorScheme.wall.selection.block;
      ctx.fill();
      ctx.strokeStyle = colorScheme.outline;
      ctx.lineWidth = 2;
      ctx.stroke();
      ctx.lineWidth = 1;
      // draw the middle lines
      const topLeft = pixelOf(coordinates.x + 0.15, coordinates.y + 0.15);
      const topRight = pixelOf(coordinates.x + 0.85, coordinates.y + 0.15);
      const bottomLeft = pixelOf(coordinates.x + 0.15, coordinates.y + 0.85);
      const bottomRight = pixelOf(coordinates.x + 0.85, coordinates.y + 0.85);
      ctx.beginPath();
      ctx.moveTo(topLeft.x, topLeft.y);
      ctx.lineTo(bottomRight.x, bottomRight.y);
      ctx.moveTo(topRight.x, topRight.y);
      ctx.lineTo(bottomLeft.x, bottomLeft.y);
      ctx.closePath();
      ctx.lineWidth = 2;
      ctx.strokeStyle = colorScheme.outline;
      ctx.stroke();
      ctx.lineWidth = 1;
    }
  };

  const pencilTurretSelection = mousePos => {
    const coordinates = coordinateOf(
      interface.lastMousePos.x,
      interface.lastMousePos.y
    );
    if (
      coordinates.x >= 0 &&
      coordinates.y >= 0 &&
      coordinates.x < game.BOARD_WIDTH &&
      coordinates.y < game.BOARD_HEIGHT
    ) {
      // draw the block
      ctx.beginPath();
      coordinateBlock(coordinates.x, coordinates.y);
      ctx.closePath();
      ctx.fillStyle = colorScheme.my.turret.selection.block;
      ctx.fill();
      ctx.lineWidth = 2;
      ctx.strokeStyle = colorScheme.outline;
      ctx.stroke();
      ctx.lineWidth = 1;

      // draw the gun base
      ctx.beginPath();
      const centerPixel = pixelOf(coordinates.x + 0.5, coordinates.y + 0.5);
      const topPixel = pixelOf(coordinates.x + 0.5, coordinates.y + 0.225);
      const topOfGunPixel = pixelOf(coordinates.x + 0.5, coordinates.y + 0.4);
      ctx.arc(
        centerPixel.x,
        centerPixel.y,
        centerPixel.y - topPixel.y,
        0,
        2 * Math.PI
      );
      ctx.closePath();
      ctx.fillStyle = colorScheme.my.turret.selection.gunBase;
      ctx.fill();

      // draw the gun
      ctx.beginPath();
      ctx.arc(
        centerPixel.x,
        centerPixel.y,
        centerPixel.y - topOfGunPixel.y,
        0,
        2 * Math.PI
      );
      ctx.closePath();
      ctx.fillStyle = colorScheme.my.turret.selection.gun;
      ctx.fill();
    }
  };

  const pencilAttackerSelection = mousePos => {
    const coordinates = coordinateOf(
      interface.lastMousePos.x,
      interface.lastMousePos.y
    );
    if (
      coordinates.x >= 0 &&
      coordinates.y >= 0 &&
      coordinates.x < game.BOARD_WIDTH &&
      coordinates.y < game.BOARD_HEIGHT
    ) {
      ctx.beginPath();
      coordinateTriangle(coordinates.x, coordinates.y);
      ctx.closePath();
      ctx.fillStyle = colorScheme.my.attacker.selection.block;
      ctx.fill();
      ctx.strokeStyle = colorScheme.outline;
      ctx.lineWidth = 2;
      ctx.stroke();
      ctx.lineWidth = 1;
      ctx.resetTransform();
    }
  };

  const pencilMineSelection = () => {
    const coordinates = coordinateOf(
      interface.lastMousePos.x,
      interface.lastMousePos.y
    );
    if (
      coordinates.x >= 0 &&
      coordinates.y >= 0 &&
      coordinates.x < game.BOARD_WIDTH &&
      coordinates.y < game.BOARD_HEIGHT
    ) {
      // draw the block
      ctx.beginPath();
      coordinateBlock(coordinates.x, coordinates.y);
      ctx.closePath();
      ctx.fillStyle = colorScheme.my.mine.selection.block;
      ctx.fill();
      ctx.lineWidth = 2;
      ctx.strokeStyle = colorScheme.outline;
      ctx.stroke();
      ctx.lineWidth = 1;

      // draw the pickaxe arc
      ctx.beginPath();
      const centerPixel = pixelOf(coordinates.x + 0.5, coordinates.y + 0.5);
      const topPixel = pixelOf(coordinates.x + 0.5, coordinates.y + 0.3);
      ctx.arc(
        centerPixel.x,
        centerPixel.y,
        centerPixel.y - topPixel.y,
        -4.5 * Math.PI / 4,
        -1.5 * Math.PI / 4
      );
      ctx.strokeStyle = colorScheme.my.mine.selection.pickaxe;
      ctx.lineWidth = 2;
      ctx.stroke();
      // draw the pickaxe handle
      const topLeftPixel = pixelOf(coordinates.x + 0.29, coordinates.y + 0.29);
      const bottomRightPixel = pixelOf(
        coordinates.x + 0.7,
        coordinates.y + 0.7
      );
      ctx.moveTo(topLeftPixel.x, topLeftPixel.y);
      ctx.lineTo(bottomRightPixel.x, bottomRightPixel.y);
      ctx.closePath();
      ctx.stroke();

      ctx.lineWidth = 1;
    }
  };

  const pencilScaffolding = (scaffolding, color) => {
    // draw the lines
    const box1TopLeft = pixelOf(scaffolding.x + 0.25, scaffolding.y + 0.31);
    const box1TopRight = pixelOf(scaffolding.x + 0.7, scaffolding.y + 0.25);
    const box1BottomLeft = pixelOf(scaffolding.x + 0.28, scaffolding.y + 0.52);
    const box1BottomRight = pixelOf(scaffolding.x + 0.73, scaffolding.y + 0.46);
    const box2TopLeft = pixelOf(scaffolding.x + 0.15, scaffolding.y + 0.17);
    const box2TopRight = pixelOf(scaffolding.x + 0.45, scaffolding.y + 0.15);
    const box2BottomLeft = pixelOf(scaffolding.x + 0.2, scaffolding.y + 0.80);
    const box2BottomRight = pixelOf(scaffolding.x + 0.51, scaffolding.y + 0.78);
    const box3TopLeft = pixelOf(scaffolding.x + 0.28, scaffolding.y + 0.62);
    const box3TopRight = pixelOf(scaffolding.x + 0.74, scaffolding.y + 0.30);
    const box3BottomLeft = pixelOf(scaffolding.x + 0.42, scaffolding.y + 0.83);
    const box3BottomRight = pixelOf(scaffolding.x + 0.89, scaffolding.y + 0.51);

    // draw box 1
    ctx.beginPath();
    ctx.moveTo(box1TopLeft.x, box1TopLeft.y);
    ctx.lineTo(box1TopRight.x, box1TopRight.y);
    ctx.lineTo(box1BottomRight.x, box1BottomRight.y);
    ctx.lineTo(box1BottomLeft.x, box1BottomLeft.y);
    ctx.lineTo(box1TopLeft.x, box1TopLeft.y);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.lineWidth = 2;
    ctx.strokeStyle = colorScheme.darkOutline;
    ctx.stroke();

    // draw box 2
    ctx.beginPath();
    ctx.moveTo(box2TopLeft.x, box2TopLeft.y);
    ctx.lineTo(box2TopRight.x, box2TopRight.y);
    ctx.lineTo(box2BottomRight.x, box2BottomRight.y);
    ctx.lineTo(box2BottomLeft.x, box2BottomLeft.y);
    ctx.lineTo(box2TopLeft.x, box2TopLeft.y);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.lineWidth = 2;
    ctx.strokeStyle = colorScheme.darkOutline;
    ctx.stroke();

    // draw box 3
    ctx.beginPath();
    ctx.moveTo(box3TopLeft.x, box3TopLeft.y);
    ctx.lineTo(box3TopRight.x, box3TopRight.y);
    ctx.lineTo(box3BottomRight.x, box3BottomRight.y);
    ctx.lineTo(box3BottomLeft.x, box3BottomLeft.y);
    ctx.lineTo(box3TopLeft.x, box3TopLeft.y);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.lineWidth = 2;
    ctx.strokeStyle = colorScheme.darkOutline;
    ctx.stroke();

    ctx.lineWidth = 1;
    pencilHealth(scaffolding, scaffolding.maxHealth);
  };

  const pencilBuildingContextMenu = color => {
    ctx.globalAlpha = 0.5;
    ctx.beginPath();
    roundRect(5 * dpi, 70 * dpi, 60 * dpi, 60 * dpi, 5 * dpi);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = colorScheme.outline;
    ctx.lineWidth = 2;
    ctx.stroke();
    ctx.lineWidth = 1;
  };

  const pencilHealth = (object, maxHealth) => {
    // console.log("object", object);    
    // maxHealth = 
    if (object.health) {
      // pencil the health
      const topLeftPixel = pixelOf(object.x, object.y + 0.9);
      const topRightBarPixel = pixelOf(object.x + 1, object.y + 0.9);
      const topRightRemainingPixel = pixelOf(
        object.x + object.health / maxHealth,
        object.y + 0.9
      );
      const bottomLeftPixel = pixelOf(object.x, object.y + 1);
      const bottomRightBarPixel = pixelOf(object.x + 1, object.y + 1);
      const bottomRightRemainingPixel = pixelOf(
        object.x + object.health / maxHealth,
        object.y + 1
      );
      ctx.beginPath();
      ctx.moveTo(topLeftPixel.x, topLeftPixel.y);
      ctx.lineTo(topRightBarPixel.x, topRightBarPixel.y);
      ctx.lineTo(bottomRightBarPixel.x, bottomRightBarPixel.y);
      ctx.lineTo(bottomLeftPixel.x, bottomLeftPixel.y);
      ctx.closePath();
      ctx.fillStyle = colorScheme.health.bar;
      ctx.fill();
      ctx.beginPath();
      ctx.moveTo(topLeftPixel.x, topLeftPixel.y);
      ctx.lineTo(topRightRemainingPixel.x, topRightRemainingPixel.y);
      ctx.lineTo(bottomRightRemainingPixel.x, bottomRightRemainingPixel.y);
      ctx.lineTo(bottomLeftPixel.x, bottomLeftPixel.y);
      ctx.closePath();
      ctx.fillStyle = colorScheme.health.remaining;
      ctx.fill();
    }
  };

  const pencilAttackerHealth = (attacker, maxHealth) => {
    if (attacker.health) {
      // pencil the health
      const topLeftPixel = pixelOf(attacker.x, attacker.y + 0.9);
      const topRightBarPixel = pixelOf(attacker.x + 1, attacker.y + 0.9);
      const topRightRemainingPixel = pixelOf(
        attacker.x + attacker.health / maxHealth,
        attacker.y + 0.9
      );
      const bottomLeftPixel = pixelOf(attacker.x, attacker.y + 1);
      const bottomRightBarPixel = pixelOf(attacker.x + 1, attacker.y + 1);
      const bottomRightRemainingPixel = pixelOf(
        attacker.x + attacker.health / maxHealth,
        attacker.y + 1
      );
      ctx.beginPath();
      ctx.moveTo(topLeftPixel.x, topLeftPixel.y);
      ctx.lineTo(topRightBarPixel.x, topRightBarPixel.y);
      ctx.lineTo(bottomRightBarPixel.x, bottomRightBarPixel.y);
      ctx.lineTo(bottomLeftPixel.x, bottomLeftPixel.y);
      ctx.closePath();
      ctx.fillStyle = colorScheme.health.bar;
      ctx.fill();
      ctx.beginPath();
      ctx.moveTo(topLeftPixel.x, topLeftPixel.y);
      ctx.lineTo(topRightRemainingPixel.x, topRightRemainingPixel.y);
      ctx.lineTo(bottomRightRemainingPixel.x, bottomRightRemainingPixel.y);
      ctx.lineTo(bottomLeftPixel.x, bottomLeftPixel.y);
      ctx.closePath();
      ctx.fillStyle = colorScheme.health.remaining;
      ctx.fill();
    }
  };

  const pencilInvalidSquare = (x, y) => {
    coordinateRect(x, y, 1, 1);
  };

  const highlightValidSquares = (boundingBoxCoordinates, objects, my) => {
    ctx.beginPath();
    coordinateRect(
      boundingBoxCoordinates.topLeft.x,
      boundingBoxCoordinates.topLeft.y,
      boundingBoxCoordinates.bottomRight.x - boundingBoxCoordinates.topLeft.x,
      boundingBoxCoordinates.bottomRight.y - boundingBoxCoordinates.topLeft.y
    );
    ctx.closePath();
    ctx.fillStyle = "rgba(20, 20, 20, 0.2)";
    ctx.fill();
    highlightAroundBase(objects.my.base, my.color);
    my.walls.forEach(wall => {
      highlightAroundWall(wall, my.color);
    });
    my.turrets.forEach(turret => {
      highlightAroundTurret(turret, my.color);
    });
    my.mines.forEach(mine => {
      highlightAroundMine(mine, my.color);
    });
  };

  const highlightAround = (object, color) => {
    // draw the grass around the object
    ctx.beginPath();
    coordinateRect(
      wall.x - game.MAX_BLOCK_DISTANCE,
      wall.y - game.MAX_BLOCK_DISTANCE,
      game.MAX_BLOCK_DISTANCE * 2 + 1,
      game.MAX_BLOCK_DISTANCE * 2 + 1
    );
    ctx.closePath();
    ctx.fillStyle = colorScheme.background;
    ctx.fill();
    // draw the grid around the object

    ctx.beginPath();
    // finding all the integers between wall.x - game.MAX_BLOCK_DISTANCE
    for (
      let i = wall.x - game.MAX_BLOCK_DISTANCE;
      i <= wall.x + game.MAX_BLOCK_DISTANCE && i <= game.BOARD_WIDTH;
      i++
    ) {
      if (i >= 0) {
        // draw a line from (i, 0) to (i, game.BOARD_HEIGHT)
        const topPixel = pixelOf(i, 0);
        const bottomPixel = pixelOf(i, game.BOARD_HEIGHT);
        ctx.moveTo(topPixel.x, topPixel.y);
        ctx.lineTo(bottomPixel.x, bottomPixel.y);
      }
    }

    for (
      let j = wall.y - game.MAX_BLOCK_DISTANCE;
      j <= wall.y + game.MAX_BLOCK_DISTANCE && j <= game.BOARD_HEIGHT;
      j++
    ) {
      if (j >= 0) {
        // draw a line from (0, j) to (game.BOARD_WIDTH, j)
        const leftPixel = pixelOf(0, j);
        const rightPixel = pixelOf(game.BOARD_WIDTH, j);
        ctx.moveTo(leftPixel.x, leftPixel.y);
        ctx.lineTo(rightPixel.x, rightPixel.y);
      }
    }
    ctx.closePath();
    ctx.strokeStyle = colorScheme.grid;
    ctx.stroke();
  };

  const highlightAroundBase = (base, color) => {
    highlightAround(base);
    pencilBase(base, color, true);
  };

  const highlightAroundWall = (wall, color) => {
    highlightAround(wall);
    pencilWall(wall, color, true);
  };

  const highlightAroundTurret = (turret, color) => {
    highlightAround(turret);
    pencilTurret(turret, color, true);
  };

  const highlightAroundMine = (mine, color) => {
    highlightAround(mine);
    pencilMine(mine, color, true);
  };

  return {
    coordinateOf,
    pixelOf,
    getColorScheme,
    setColorScheme,
    getCanvas,
    setCanvas,
    getCtx,
    setCtx,
    getDpi,
    setDpi,
    getBoundingBoxCoordinates,
    setBoundingBoxCoordinates,
    pencilBackground,
    pencilGrid,
    pencilBase,
    pencilWall,
    pencilSelectedWall,
    pencilWallSelection,
    pencilTurret,
    pencilSelectedTurret,
    pencilTurretSelection,
    pencilAttacker,
    pencilSelectedAttacker,
    pencilAttackerSelection,
    pencilMine,
    pencilSelectedMine,
    pencilMineSelection,
    pencilScaffolding,
    pencilResource,
    pencilInvalidSquare,
    highlightValidSquares,
    pencilBuildingContextMenu,
    pencilHud
  };
};
