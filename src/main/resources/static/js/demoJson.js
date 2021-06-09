let byIdObjects = {
  type: 5,
  //etc.
};


let objects = {
  my: {
    boundingBox: {
      topLeft: { x: -20.5, y: -6.13412759415834 },
      bottomRight: { x: 39.5, y: 25.13412759415834 }
    },
    statistics: {
      name: "Mac",
      resources: 72
    },
    base: { x: 5, y: 5, health: 100 },
    walls: [
      { x: 4, y: 5, health: 100 },
      { x: 5, y: 4, health: 100 },
      { x: 5, y: 6, health: 100 },
      { x: 6, y: 5, health: 100 }
    ],
    turrets: [
      { x: 4, y: 4, health: 100 },
      { x: 4, y: 6, health: 100 },
      { x: 6, y: 4, health: 100 },
      { x: 6, y: 6, health: 100 }
    ],
    attackers: [
      {
        x: 6,
        y: 10,
        health: 100,
        movement: { direction: 3, ratio: 0.8 }
      },
      {
        x: 5,
        y: 11,
        health: 100,
        movement: { direction: 3, ratio: 0.8 }
      }
    ]
  },
  others: {
    bases: [{ x: 20, y: 20, health: 100 }, { x: 72, y: 34, health: 100 }],
    walls: [{ x: 19, y: 20, health: 100 }, { x: 21, y: 20, health: 100 }],
    turrets: [{ x: 19, y: 19, health: 100 }],
    attackers: [
      { x: 17, y: 17, health: 100, movement: { direction: 7, ratio: 0 } }
    ]
  },
  resources: [{ x: 2, y: 2, health: 100 }, { x: 20, y: 23, health: 100 }]
};
