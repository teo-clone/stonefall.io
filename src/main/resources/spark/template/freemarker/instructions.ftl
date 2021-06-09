<!DOCTYPE html>
<head>
  <meta charset="utf-8">
  <title>${title}</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- In real-world webapps, css is usually minified and
  concatenated. Here, separate normalize from our code, and
  avoid minification for clarity. -->
  <link rel="stylesheet" href="css/normalize.css">
  <link rel="stylesheet" href="css/html5bp.css">
  <link rel="stylesheet" href="css/login.css">
  <link rel="stylesheet" href="css/gameover.css">
  <link rel="stylesheet" href="css/buttons.css">
  <link rel="stylesheet" href="css/instructions.css">

  <link href='https://fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>
</head>
<body>
  <div id="inGod">
    <h1 id="inGodHeader">Welcome to Stonefall</h1>
    <h2 id="inGodSubHeader">Read on for a brief tutorial</h2>
    <a href="#inLeftStrip1" id="inGodDownArrowLink">
      <img src="\img\leftarrow.png" alt="wallImage" id="inGodDownArrow" class="inGodDownArow">
    </a>
    <!-- a left strip  -->
    <div id="inLeftStrip1" class="inLeftStrip" >
      <!-- honestly not sure why this works with css flexbox but it does -->
      <div class="inLeftStripBody" >
        <!-- the text in the left strip  -->
        <div id="inLeftStripText1" class="inLeftStripText">
          <h2 class="inStripHeader"> Your Base </h2>
          <p class="inStripSmallText"> This is your base. Protect it by building things! </p>
          <img src="\img\tutorialBase.png" alt="wallImage" id="inLeftStripImage1" class="inLeftStripImage">
        </div>
      </div>
    </div>
    <!-- a right strip  -->
    <div id="inRightStrip1" class="inRightStrip">
      <div class="inRightStripBody" >

      <!-- text in right strip  -->
      <div id="inRightStripText1" class="inRightStripText">
        <h2 class="inStripHeader"> Resources </h2>
        <p class="inStripSmallText"> Everything you need to create your empire costs resources. Keep track of your resources here. </p>
      <img src="\img\tutorialTrackResources.png" alt="wallImage" id="inRightStripImage1" class="inRightStripImage">
    </div>

    </div>
    </div>

    <!-- a left strip  -->
    <div id="inLeftStrip1" class="inLeftStrip" >
      <div class="inLeftStripBody" >

      <!-- the text in the left strip  -->
      <div id="inLeftStripText1" class="inLeftStripText">
        <h2 class="inStripHeader"> Resources continued... </h2>
        <p class="inStripSmallText"> How do you get resources? Go down here to the selection menu and pick "mine" </p>
      <img src="\img\tutorialMinerIcon.png" alt="wallImage" id="inLeftStripImage1" class="inLeftStripImage">
    </div>

    </div>
    </div>

    <!-- a right strip  -->
    <div id="inRightStrip1" class="inRightStrip">
      <div class="inRightStripBody" >

      <!-- text in right strip  -->
      <div id="inRightStripText1" class="inRightStripText">
        <h2 class="inStripHeader"> Resources continued </h2>
        <p class="inStripSmallText"> Once you've built a mine and placed it on the board next to a resource,
          you should start seeing your resources increase! <br> Note that you can only build new structures in the non-shaded area; within 4 blocks of your other structures.</p>
      <img src="\img\tutorialSelectMiner.png" alt="wallImage" id="inRightStripImage1" class="inRightStripImage">
    </div>

    </div>
  </div>

    <!-- the next left strip  -->
    <div id="inLeftStrip2" class="inLeftStrip" >
      <div class="inLeftStripBody" >

      <!-- the text in the left strip  -->
      <div id="inLeftStripText2" class="inLeftStripText">
        <h2 class="inStripHeader"> Defend your base </h2>
        <p class="inStripSmallText"> Once you have enough resources, build a turret.
          Turrets can kill attackers from a distance, but can be killed very easily!
          They are especially vulnerable while they are being built so try not to build close
          to other attackers. </p>
      <img src="\img\tutorialTurret.png" alt="wallImage" id="inLeftStripImage2" class="inLeftStripImage">
    </div>

      </div>
    </div>

    <!-- the next right strip  -->
    <div id="inRightStrip2" class="inRightStrip">      <div class="inRightStripBody" >

      <!-- text in right strip  -->
      <div id="inRightStripText2" class="inRightStripText">
        <h2 class="inStripHeader"> Walls </h2>
        <p class="inStripSmallText"> Walls are cheap and have lots of health. Surround your turrets with walls. </p>
      <img src="\img\tutorialWalls.png" alt="wallImage" id="inRightStripImage2" class="inRightStripImage">
    </div>
  </div>
</div>

    <!-- the next left strip  -->
    <div id="inLeftStrip2" class="inLeftStrip" >
      <div class="inLeftStripBody" >

      <!-- the text in the left strip  -->
      <div id="inLeftStripText2" class="inLeftStripText">
        <h2 class="inStripHeader"> Walls continued... </h2>
        <p class="inStripSmallText"> Note that attackers can travel diagonally, so this is <b> NOT </b> a safe wall placement. </p>
      <img src="\img\tutorialBadPlacement.png" alt="wallImage" id="inLeftStripImage2" class="inLeftStripImage">
    </div>
</div>
    </div>

    <!-- the next right strip  -->
    <div id="inRightStrip2" class="inRightStrip">
      <div class="inRightStripBody" >

      <!-- text in right strip  -->
      <div id="inRightStripText2" class="inRightStripText">
        <h2 class="inStripHeader"> Cost inflation </h2>
        <p class="inStripSmallText"> You may have noticed by now that some of your building costs have risen. They rise
        in proportion to your score (which you can find in the top left corner). </p>
      <img src="\img\tutorialCostInflation.png" alt="wallImage" id="inRightStripImage2" class="inRightStripImage">
    </div>
</div>
    </div>

    <!-- the next left strip  -->
    <div id="inLeftStrip2" class="inLeftStrip" >
      <div class="inLeftStripBody" >

      <!-- the text in the left strip  -->
      <div id="inLeftStripText2" class="inLeftStripText">
        <h2 class="inStripHeader"> Attack! </h2>
        <p class="inStripSmallText"> Wouldn't it be nice to get resources another way? Well, every time you kill somebody else's stuff,
        you get resources. The higher their score, the more resources you get.  </p>
      <img src="\img\tutorialAttackPrep.png" alt="wallImage" id="inLeftStripImage2" class="inLeftStripImage">
    </div>
    </div>
  </div>


    <!-- the next right strip  -->
    <div id="inRightStrip2" class="inRightStrip">
    <div class="inRightStripBody" >

      <!-- text in right strip  -->
      <div id="inRightStripText2" class="inRightStripText">

        <h2 class="inStripHeader"> Build attackers </h2>
        <p class="inStripSmallText"> Select an attacker
        and place one on the map.  </p>
      <img src="\img\tutorialBuildOne.png" alt="wallImage" id="inRightStripImage2" class="inRightStripImage">
    </div>
    </div>
  </div>

    </div>

    <!-- a left strip  -->
    <div id="inLeftStrip1" class="inLeftStrip" >
      <div class="inLeftStripBody" >

      <!-- the text in the left strip  -->
      <div id="inLeftStripText1" class="inLeftStripText">
        <h2 class="inStripHeader"> Attack! </h2>
        <p class="inStripSmallText"> Select your attacker by left clicking.  </p>
      <img src="\img\tutorialSelectOne.png" alt="wallImage" id="inLeftStripImage1" class="inLeftStripImage">
    </div>
  </div>

    </div>

    <!-- a right strip  -->
    <div id="inRightStrip1" class="inRightStrip">
      <div class="inRightStripBody" >

      <!-- text in right strip  -->
      <div id="inRightStripText1" class="inRightStripText">
        <h2 class="inStripHeader"> Attack, continued </h2>
        <p class="inStripSmallText"> Select multiple attackers by holding shift while left clicking. </p>
      <img src="\img\tutorialSelectMultiple.png" alt="wallImage" id="inRightStripImage1" class="inRightStripImage">
    </div>
  </div>

    </div>

    <!-- a left strip  -->
    <div id="inLeftStrip1" class="inLeftStrip" >
      <div class="inLeftStripBody" >

      <!-- the text in the left strip  -->
      <div id="inLeftStripText1" class="inLeftStripText">
        <h2 class="inStripHeader"> Attack, continued </h2>
        <p class="inStripSmallText"> Now right click on your target to attack! <br> If you don't see any enemies on your screen, explore the map using the arrow keys or by dragging to find some. The map is very large, so if you lose your base at any point hit the space bar to return to it.</p>
      <img src="\img\tutorialAttack.png" alt="wallImage" id="inLeftStripImage1" class="inLeftStripImage">
    </div>
  </div>

    </div>

    <!-- a right strip  -->
    <div id="inRightStrip1" class="inRightStrip">
      <div class="inRightStripBody" >

      <!-- text in right strip  -->
      <div id="inRightStripText1" class="inRightStripText">
        <h2 class="inStripHeader"> You're almost ready to play! </h2>
        <p class="inStripSmallText"> Sell buildings by selecting a building and then clicking the sell button on the top left.  </p>
      <img src="\img\tutorialSellBuilding.png" alt="wallImage" id="inRightStripImage1" class="inRightStripImage">
    </div>
  </div>

    </div>

    <!-- a left strip  -->
    <div id="inLeftStrip1" class="inLeftStrip" >
      <div class="inLeftStripBody" >

      <!-- the text in the left strip  -->
      <div id="inLeftStripText1" class="inLeftStripText">
        <h2 class="inStripHeader"> You're ready! </h2>
        <p class="inStripSmallText"> If you ever got lost on the map, press the space bar to return to home. Explore some of the
          other units in the selection bar.
          Have fun, try to get to the top of the leaderboard, and let the stones fall! </p>
      <img src="\img\tutorialBaserando.png" alt="wallImage" id="inLeftStripImage1" class="inLeftStripImage">
    </div>

  </div>

    </div>

    <!-- let em play the game son!  -->

        <!-- a right strip  -->
        <div id="inRightStrip1" class="inRightStrip">
          <div class="inRightStripBody" >

          <!-- text in right strip  -->
          <div id="inRightStripText1" class="inRightStripText">
            <!-- <h2 class="inS tripHeader"> You're almost ready to play! </h2> -->

          </div>
          <a id="inPlay" class="button button-primary button-pill" href="/">Play</a>

      </div>






  </div>
  <!-- Again, we're serving up the unminified source for clarity. -->
  <script src="js/jquery-3.1.1.js"></script>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
dealing with real world issues like old browsers.  -->
</html>
