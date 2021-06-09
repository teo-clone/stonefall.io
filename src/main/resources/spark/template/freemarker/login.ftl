
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
  <link rel="stylesheet" href="css/buttons.css">
  <link href='https://fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>
</head>
<body>


  <div id="main">
    <h1>Stonefall</h1>

    <!-- form for user to input name  -->
    <form action="/" method="POST" id="loginForm">
      <input type="text" id="myName" name="myName" placeholder="Enter your name here"><br>
      <input type="submit" class="button button-action button-pill" id="submitButton" value="Go">
    </form>

    <!-- instructions -->
    <h3 id="instructionsHeader"> Never played? </h3>
    <!-- <p id="instructions"> -->
      <a id="tutorialButton" class="button button-primary button-pill" href="/instructions">Tutorial</a>

    <!--  <div style = "position: fixed; top: 5px; right: 5px">
        <a id="donateButton" class="button button-plain button-pill" onclick="getElementById('donateForm').style.display = ((getElementById('donateForm').style.display==='none') ? 'block' : 'none')">Donate</a>
        <div id = "donateForm">
          <script src="https://donorbox.org/widget.js" paypalExpress="false"></script><iframe src="https://donorbox.org/embed/keep-the-severs-running?hide_donation_meter=true" height="685px" width="100%" style="max-width:500px; min-width:310px; max-height:none!important" seamless="seamless" name="donorbox" frameborder="0" scrolling="no" allowpaymentrequest></iframe>
        </div>
      </div> -->


        <!-- (1) Mine resources by building a mine next to a rock <br>
        (2) Build walls near your base to stop people from attacking you <br>
        (3) Build turrets behind those walls to defend yourself <br>
        (4) Build attackers to go kill people. <br> -->
        <!-- Select attackers
        by left clicking them, and attack by right clicking on an enemy structure. <br>
        Get resources from destroying other people's structures (the bigger they are, the more you get). <br>
        Look around with the arrow keys. <br> -->
        <!-- Have fun, and let the stones fall. <br> -->
     <!-- < /p> -->
      <div>
        <a id="aboutButton" class="button button-secondary button-small button-pill" href="/hq"> About Us</a>
     </div>
     
     <div style = "position: fixed; bottom: 5px; left: 5px">
     <a id="createdBy" href="https://fabricegs.github.io/index.html" > Built by Fabrice Guyot-Sionnest, David Oyeka, Teo Tsivranidis & Mac Mccann</a>
     </div>
  </div>


  <!-- clear the form so another user can use it -->
  <script type="text/javascript">
    document.getElementById("loginForm").reset();
  </script>

  <!-- Again, we're serving up the unminified source for clarity. -->
  <script src="js/jquery-3.1.1.js"></script>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
dealing with real world issues like old browsers.  -->
</html>
