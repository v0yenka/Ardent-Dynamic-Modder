package org.cyberagent;

public class Config {
    public Cheats cheats;
    public String serverVersion;

    public static class Cheats {
        public boolean infiniteStamina;
        public boolean noFallDamage;
        public int xpMultiplier;
        public boolean unlockAllDragons;
        public boolean patchKnownBugs;
    }
}
