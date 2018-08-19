package swingy.common;

/*
 * CAN HAVE 1 OR 2 AT THE SAME TIME
 * MARTIAL:
 *  - weapon: ignore some (%) armor <KEEN >
 *  - armor: extra armor <OF DEFENSE>
 *  - helmet: extra hp <OF VIGOR>
 * ELEMENTAL: <OF ... FIRE, FROST, SHOCK>
 *  - weapon: add elemental damage [FIRE, FROST, SHOCK]
 *  - armor: enemies have a chance (%) to take damage
 *  - helmet: you have a chance to make a free attack, doing half damage, of which HALF ELEMENTAL
 * CURSED: <OF DOOM>
 *  - weapon: chance to do double damage, lower chance to do half damage
 *  - armor: chance to take half damage, lower chance to take double damage
 *  - helmet: chance to absorb some damage and HEAL self, lower chance to absorb damage BUT HEAL ENEMY
 */
public enum ItemType {
    NONE, MARTIAL, ELEMENTAL_FIRE, ELEMENTAL_FROST, ELEMENTAL_SHOCK, CURSED
}
