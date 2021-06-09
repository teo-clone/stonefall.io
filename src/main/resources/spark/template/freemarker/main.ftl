<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-123481528-2"></script>
    <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());

      gtag('config', 'UA-123481528-2');
    </script>
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/game.css">
    <link href='https://fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>
  </head>
  <body>
    <#include "game.ftl">
    <!-- Again, we're serving up the unminified source for clarity. -->
    <script src="js/jquery-3.1.1.js"></script>
    <script src="js/Penciller.js"></script>
    <script src="js/BoundingBox.js"></script>
    <script src="js/SingletonCanvas.js"></script>
    <script src="js/WebSockets.js"></script>
    <script src="js/Game.js"></script>
    <script src="js/Handlers.js"></script>
    <script src="js/canvas.js"></script>
    <script src="js/main.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
