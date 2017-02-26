# RusHour
## Inspiration
We were thinking about problems that machines could solve better than humans, and we realized no human could ever truly comprehend a city's traffic patterns. Each light, each road, each car affects every other. A network of learning machines, however, could consider and discard more possibilities than a human ever could.
## What it does
A city is generated from a basic road pattern, and cars are placed throughout, with destinations chosen from the grid. The cars must use pathfinding algorithms to make their way to the destination. Along the way, traffic lights have to learn to balance each of their inputs and outputs.
## How we built it
The project is built in Java, using mostly the standard library and our own code. A library called Jama was added for matrix math, but otherwise we were dependency-free.

The A* Pathfinding algorithm was used to calculate the path for each car, which means they will follow an optimal path to their destination.

Neural Networks with batch-gradient descent implemented reinforcement learning in the traffic lights to adjust their behavior to meet demand. 

The graphics are drawn with Java2D and the art was all created for this project.

## Challenges we ran into

While we had some familiarity with the techniques we intended to employ, we certainly aren't experts. A* required advanced graph theory reasoning which falls outside of our comfort zone. By far our biggest obstacle was the neural networks themselves; which required matrix math, calculus, and a bit of multi-variable calculus (which we haven't yet encountered in school.) 

## Accomplishments that we're proud of

The city is created, cars move through it, and the traffic lights change at a pattern that allows smooth flow of vehicles. The core of our application is very much there.

## What we learned

In the words of a neural network resource we found: "Don't stop doing the chain rule, ever." In all seriousness, we learned a bit of a calculus, a bit of linear algebra, an application for the highest-level math we've ever encountered, how to apply machine learning, and some highway safety.

## What's next for RusHour

Originally we planned for a much higher degree of fidelity in the simulation, including specific people with specific housing units and specific places of work. Additionally, we would like to load real-life road patterns into the project. Possibly, we could also add some interesting off-road features to make the application more aesthetically pleasing. 
