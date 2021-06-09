const BoundingBox = function(initWidth, initHeight, initCenter) {
  let topLeft;
  let bottomRight;
  const width = initWidth;
  const height = initHeight;
  let center = initCenter;
  const MAXIMUM_VERTICAL_VIEW_UNITS = 20;
  const MAXIMUM_HORIZONTAL_VIEW_UNITS = 40;

  const clip = (x, a, b) => {
    if (x < a) return a;
    if (x > b) return b;
    return x;
  };

  const calculateBoundingBox = () => {
    // We need to calculate a bounding box such that:
    // 1) the bounding box ratio is 1:1 with the canvas ratio
    // 2) the player does not see more than MAXIMUM_VERTICAL_VIEW_UNITS up
    // 3) the player does not see more than MAXIMUM_HORIZONTAL_VIEW_UNITS across

    // calculate the unit ratio for both
    const horizontalCanvasUnitsRatio = MAXIMUM_HORIZONTAL_VIEW_UNITS / width;
    const verticalCanvasUnitsRatio = MAXIMUM_VERTICAL_VIEW_UNITS / height;
    // choose the dimension with the lower unit ratio
    if (horizontalCanvasUnitsRatio < verticalCanvasUnitsRatio) {
      // we're using horizontalCanvasUnitsRatio as our ratio
      topLeft = {
        x: center.x - MAXIMUM_HORIZONTAL_VIEW_UNITS / 2,
        y: center.y - height * horizontalCanvasUnitsRatio / 2
      };
      bottomRight = {
        x: center.x + MAXIMUM_HORIZONTAL_VIEW_UNITS / 2,
        y: center.y + height * horizontalCanvasUnitsRatio / 2
      };
    } else {
      // we're using verticalCanvasUnitsRatio as our ratio
      topLeft = {
        x: center.x - width * verticalCanvasUnitsRatio / 2,
        y: center.y - MAXIMUM_VERTICAL_VIEW_UNITS / 2
      };
      bottomRight = {
        x: center.x + width * verticalCanvasUnitsRatio / 2,
        y: center.y + MAXIMUM_VERTICAL_VIEW_UNITS / 2
      };
    }
  };

  const updateFromDelta = delta => {
    const horizontalShift = delta.x / width * (bottomRight.x - topLeft.x);
    const verticalShift = delta.y / height * (bottomRight.y - topLeft.y);
    center = {
      x: clip(center.x - horizontalShift, 0, game.BOARD_WIDTH),
      y: clip(center.y - verticalShift, 0, game.BOARD_HEIGHT)
    };
    calculateBoundingBox();
  };

  const getCoordinates = () => {
    return {
      topLeft,
      bottomRight
    };
  };

  const setCenter = newCenter => {
    center = newCenter;
  };
  const getCenter = () => center;

  calculateBoundingBox();

  const contains = object => {
    if (
      object.x >= Math.floor(topLeft.x) &&
      object.x <= Math.ceil(bottomRight.x) &&
      object.y >= Math.floor(topLeft.y) &&
      object.y <= Math.ceil(bottomRight.y)
    ) {
      return true;
    } else {
      return false;
    }
  };

  return {
    getCoordinates,
    setCenter,
    getCenter,
    updateFromDelta,
    contains
  };
};
