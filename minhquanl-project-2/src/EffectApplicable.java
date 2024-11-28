/**
 *  An interface that is implemented by object that the effect (i.e. Coin and Star) is applicable
*/

public interface EffectApplicable {
    public void collectPower(Coin coin);
    public void collectStar(InvisibleStar star);
    public boolean isInvisible();
}
