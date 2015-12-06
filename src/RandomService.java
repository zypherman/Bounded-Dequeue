import java.util.Random;

/**
 * Class to generate random numbers for us
 */
public class RandomService {

    private int mean;
    private int distribution;
    Random random;

    /**
     * Constructor for the random class
     *
     * @param mean int
     * @param distribution int
     */
    public RandomService(int mean, int distribution) {
        this.mean = mean;
        this.distribution = distribution;
        this.random = new Random();
    }

    public RandomService() {
        this.random = new Random();
    }

    /**
     * Generates a random number with a given mean and distribution
     * @return int
     */
    public int getRandom() {
        int randomValue = (int) Math.round(((random.nextGaussian() * distribution) + mean));
        if(randomValue < 0) { // If we got a negative call this method again
            getRandom();
        }
        return randomValue;
    }

    /**
     * Generates a random number with a given mean and distribution
     * Second method signature for fun
     * @param mean int
     * @param distribution int
     * @return int
     */
    public int getRandom(int mean, int distribution) {
        int randomValue = (int) Math.round(((random.nextGaussian() * distribution) + mean));
        if(randomValue < 0) { // If we got a negative call this method again
            getRandom(mean, distribution);
        }
        return randomValue;
    }
}
