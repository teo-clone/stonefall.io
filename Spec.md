# Pre-game Page
This page contains an input field where user enters their base name.
There is a start button.
If 100 players are already playing, then the start button is blurred out.
If not, then after clicking the start button, a minimap shows up that shows where all the current users are located.
Then, the user can select a starting grid square (by clicking a square on the minimap) and put their base on that square.
If the user tries to click a square that already has a gridblock on it, there will be a red icon over the cursor indicating that the user cannot place a base there.
# Game Page
## Misc.
When mouse reaches the edge of the screen, the map scrolls in the edge direction until mouse leaves the edge.
Player starts out with a base and 3 workers. The base appears as a 4 grid-square heart, and the workers appear
The base has 100 health and when the base health reaches 0 the player has lost.
There will be a small high score modal in the upper right corner showing which bases have collected the most resources.
## Workers
The workers will have two modes: free roam, and stay in base.
When free roam is enabled, workers automatically choose the nearest resource to the heart that no other workers have targeted and try to collect that resource. When a worker collects a resource (by going to it, collecting to it, and coming back to base to drop it off), the resource count will increase by N.
When stay in base is enabled, workers will stay next to the base, doing nothing.
## Resources
The player will be able to buy more workers, attackers, and structures (wall, turret) using the resources that they have collected with the workers.
If there is not enough resources to buy an item, the item will be greyed out.
Resources will be automatically be generated in random empty grid squares as time goes by.
Workers and attackers will be able to pass over the player’s own defense blocks. However, they will not be able to walk over enemy blocks.
## Attacking
The player can instruct their attackers to attack an enemy’s heart by clicking on an enemy base. Attackers will be able to attack an enemy structures (base, turret, wall) either from two squares away with damage M, or from one square away with damage N, such that N > M. Attackers will have a smart attacking technique. When enemy structures are destroyed by attackers, the player who performs the “last hit” on the enemy structure collects resource rewards depending on what structure the player destroyed.
## Defending
When building defense blocks, build mode will be optionally enabled, indicating with red or green where new blocks may be placed. They will not be able to be placed over other structures or over enemy attackers/workers. Additionally, new structures will only be able to be built within 3 units of any of the player’s structures. This feature is in order to disallow trapping far away enemy units into structures.
All structures will have a health bar and when attacked and their health reaches 0 they will disappear, and allow another structure to be built over their location after 3 seconds of their destruction.
Turrets will have a range of 3 GridBlocks and will be able to fire over walls. If there is an Enemy attacker within 3 GridBlocks of the turrets, the turret will target the enemy attacker closest to the heart and deal N damage per second to them.
# Post-game Page
There will be a post-game screen which shows the player:
 - What place they ended at
 - How many other bases they destroyed
 - How long they survived
 - How many resources they collected overall
