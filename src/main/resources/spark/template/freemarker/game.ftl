<canvas id="game"></canvas>

<!-- display user's name -->
<#escape x as x?html>
<h3 style="display:none;"id="name">${Name}</h3>
</#escape>

<!-- sell image -->
<img src="/img/dollar_sign_icon.png" id="sellImage">

<p id="alertBox"> Alert.  </p>

<!-- costs for creation -->
<p id="wallCost"> 100 </p>
<p id="turret1Cost"> 500 </p>
<p id="mineCost"> 1000 </p>
<p id="attacker1Cost"> 250 </p>

<!-- hotkeys for creation -->
<p id="wallHotkey"> E </p>
<p id="turret1Hotkey"> D </p>
<p id="mineHotkey"> S </p>
<p id="attacker1Hotkey"> F </p>

<!-- insert creation image tags -->
<img src="\img\walltransparent.png" alt="wallImage" id="wallImage" class="objectCreationImage">
<div class="tooltiptext" id="walltooltip">
  <p class="tooltipheader"> Wall </p>
  <p class="tooltipbody"> Cheap, lots of health. Use these to block invaders. <br> <i> Note </i>
    :  two diagonal walls won't
    stop an attacker. </p>
</div>
<img src="\img\turret1transparent.png" alt="turretImage" id="turretImage" class="objectCreationImage">
<div class="tooltiptext" id="turrettooltip">
  <p class="tooltipheader"> Turret </p>
  <p class="tooltipbody"> Defensive turrets. Deadly to attackers within 3 squares, but they are easily killed. <br>
    <i> Note </i>
    :  protect these with walls. </p>
</div>
<img src="\img\attacker1transparent.png" alt="attacker1Image" id="attacker1Image" class="objectCreationImage">
<div class="tooltiptext" id="attackertooltip">
  <p class="tooltipheader"> Attacker </p>
  <p class="tooltipbody"> Simple attackers. Use these to build your army. </p>
</div>
<img src="\img\minetransparent.png" alt="mineImage" id="mineImage" class="objectCreationImage">
<div class="tooltiptext" id="minetooltip">
  <p class="tooltipheader"> Mine </p>
  <p class="tooltipbody"> A mine is the backbone of your economy. Put them next to resources and they will automatically mine for you. </p>
</div>
