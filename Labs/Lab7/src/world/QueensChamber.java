package world;

import bee.Drone;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The queen's chamber is where the mating ritual between the queen and her
 * drones is conducted.  The drones will enter the chamber in order.
 * If the queen is ready and a drone is in here, the first drone will
 * be summoned and mate with the queen.  Otherwise the drone has to wait.
 * After a drone mates they perish, which is why there is no routine
 * for exiting (like with the worker bees and the flower field).
 *
 * @author Sean Strout @ RIT CS
 * @author Michael Jansen
 */
public class QueensChamber {
    private ConcurrentLinkedQueue<Drone> drones;

    /**
     * Create the chamber. Initially there are no drones in the chamber and the queen is not ready to mate.
     */
    public QueensChamber(){
        this.drones = new ConcurrentLinkedQueue<>();
    }

    /**
     * At the end of the simulation the queen uses this routine repeatedly
     * to dismiss all the drones that were waiting to mate. #rit_irl...
     */
    public void dismissDrone(){
        Drone drone = this.drones.poll();
        synchronized (drone){
            drone.notifyAll();
        }
    }

    /**
     * A drone enters the chamber. The first thing you should display is:
     *
     * *QC* {bee} enters chamber
     *
     * The bees should be stored in some queue like collection.
     * If the queen is ready and this drone is at the front of the collection,
     * they are allowed to mate. Otherwise they must wait. The queen isn't into any of this kinky
     * multiple partner stuff so while she is mating with a drone, she is not ready to mate again.
     * When the drone leaves this method, display the message:
     *
     * *QC* {bee} leaves chamber
     * @param drone the drone who just entered the chamber
     */
    public void enterChamber(Drone drone){
        System.out.println("*QC* " + drone + " enters chamber");
        this.drones.add(drone);

        while(this.drones.contains(drone)) {
            try {
                synchronized (drone) {
                    drone.wait();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println("*QC* " + drone + " leaves chamber");
    }

    /**
     * Are there any waiting drones? The queen uses this to check if she can mate,
     * and also in conjunction with dismissDrone().
     * @return if a drone is still waiting
     */
    public boolean hasDrone(){
        return !this.drones.isEmpty();
    }

    /**
     * When the queen is ready, they will summon the next drone from the collection
     * (if at least one is there). The queen will mate with the first drone and display a message:
     *
     * *QC* Queen mates with {bee}
     *
     * It is the job of the queen if mating to notify all of the waiting drones so that the
     * first one can be selected since we can't control which drone will unblock.
     * Doing a notify will lead to deadlock if the drone that unblocks is not the front one.
     */
    public void summonDrone(){
        Drone drone = this.drones.poll();
        System.out.println("*QC* Queen mates with " + drone);
        synchronized (drone){
            drone.setMated();
            drone.notifyAll();
        }
    }
}