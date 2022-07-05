# Narrow Bridge Crossing Problem
Consider a narrow bridge that can only allow X cars in the same direction to cross at the same time. If there are X vehicles on the bridge, any incoming vehicle must wait as shown below.\
When a vehicle exits the bridge, we have two cases to consider:
* **Case 1** there are other vehicles on the bridge
> In this case, one vehicle in the same direction should be allowed to proceed
* **Case 2** the exiting vehicle is the last one on the bridge
> In this case, the exiting vehicle is the last vehicle on the bridge:
> * If there are vehicles waiting in the opposite direction, one of them should be allowed to proceed
> * If there is no vehicle waiting in the opposite direction, then let the waiting vehicle in the same direction proceed