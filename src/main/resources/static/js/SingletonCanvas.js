const SingletonCanvas = function() {
  let dpi;
  let canvas;
  let ctx;

  const getDpi = () => {
    return dpi;
  };
  const setDpi = newDpi => {
    dpi = newDpi;
  };
  const getCanvas = () => {
    return canvas;
  };
  const setCanvas = newCanvas => {
    canvas = newCanvas;
    const style_height = +getComputedStyle(canvas)
      .getPropertyValue("height")
      .slice(0, -2);
    const style_width = +getComputedStyle(canvas)
      .getPropertyValue("width")
      .slice(0, -2);
    canvas.height = style_height * dpi;
    canvas.width = style_width * dpi;
    ctx = canvas.getContext("2d");
  };
  const getCtx = () => {
    return ctx;
  };
  const getWidth = () => {
    return canvas.width;
  }
  const getHeight = () => {
    return canvas.height;
  }

  return {
    getDpi,
    setDpi,
    getCanvas,
    setCanvas,
    getCtx,
    getWidth,
    getHeight
  };
};
