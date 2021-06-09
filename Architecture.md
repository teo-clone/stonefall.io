## Objects
```
GridLocation
  getX()
  getY()

Game: GameMap, Map<String, Player>
  tick()

GameMap: HashMap<GridLocation, GridSquare>
  GridSquare (contains GridBlock):
    getGridLocation()
    setBlock()
    getBlock()

    Resource implements GridBlock:
      collect()
      getCollector()

Player
  Map<String, Wall>
  Map<String, Base>
  Map<String, Turret>
  Map<String, Attacker>
  Map<String, Worker>

  tick()

  Wall implements Killable, GridBlock:
    interface methods

  Base implements Killable, GridBlock:
    interface methods

  Turret implements Killable, Gridblock:
    interface methods
    attack(Movable m)

    Attacker implements Killable, Movable:
      attack(GridBlock g)

    Worker implements Killable, Movable:
      collect(Resource r)
```

## Interfaces
```
Killable
  * getHealth()
  * setHealth()
  * isDead()
  * getKiller()
Movable
  * moveTo()
  * whereAmI()
GridBlock
  * getGridLocation()
```
