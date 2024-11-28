/**
 * An interfaces that is implemented by Entity of the games that can deal damage on other main objects
 */

public interface Attackable {
    public void inflictingDamage(GameMainEntity other);
}
