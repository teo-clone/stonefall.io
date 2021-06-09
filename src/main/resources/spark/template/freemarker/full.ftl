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
  <link rel="stylesheet" href="css/login.css">
  <link rel="stylesheet" href="css/gameover.css">
  <link rel="stylesheet" href="css/buttons.css">
  <link href='https://fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>
</head>
<body>
  <div id="main">
    <h1 id="serversFull" >Our servers are currently full. Please try again later. </h1>
    <h2 id="seversFullNote" > Note: We're working on building multiple rooms so this doesn't happen again. </h2>
    <a class="button button-primary button-pill" href="/">Try again?</a>
  </div>
  <!-- Again, we're serving up the unminified source for clarity. -->
  <script src="js/jquery-3.1.1.js"></script>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
dealing with real world issues like old browsers.  -->
</html>
