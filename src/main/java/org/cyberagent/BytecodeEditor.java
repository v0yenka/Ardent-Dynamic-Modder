package org.cyberagent;

import javassist.*;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.io.ByteArrayInputStream;

public class BytecodeEditor implements ClassFileTransformer {

    private Config config;

    public BytecodeEditor(Config config) {
        this.config = config;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        String normalizedName = className.replace("/", ".");

        try {
            ClassPool pool = ClassPool.getDefault();

            //just for IntelliJ testing
            if (normalizedName.equals("org.cyberagent.Main")) {
                CtClass ctClass = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtMethod method = ctClass.getDeclaredMethod("printStatus");

                //checking the json settings
                if (config.cheats.infiniteStamina) {
                    method.insertBefore("{ System.out.println(\"[Project Edge] Infinite stamina is on!\"); }");
                }

                if (config.cheats.noFallDamage) {
                    method.insertBefore("{ System.out.println(\"[Project Edge] No fall damage is on!\"); }");
                }

                if (config.cheats.xpMultiplier > 1) {
                    method.insertBefore("{ System.out.println(\"[Project Edge] Global XP Multiplier: x" + config.cheats.xpMultiplier + "\"); }");
                }
                if (config.cheats.unlockAllDragons) {
                    method.insertBefore("{ System.out.println(\"[Project Edge] All Dragons Unlocked!\"); }");
                }
                if (config.cheats.patchKnownBugs) {
                    method.insertBefore("{ System.out.println(\"[Project Edge] Server crash prevention ACTIVE.\"); }");
                }

                return ctClass.toBytecode();
            }
            //server-sided testing
            if (config.cheats.xpMultiplier > 1 && normalizedName.equals("com.projectedge.server.ExperienceManager")) {
                CtClass ctClass = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtMethod method = ctClass.getDeclaredMethod("addExperience");
                //the first argument is multiplied by the config value
                method.insertBefore("{ $1 = $1 * " + config.cheats.xpMultiplier + "; }");
                System.out.println("[+] Injected! XP multiplied by: x" + config.cheats.xpMultiplier);
                return ctClass.toBytecode();
            }


            if (config.cheats.unlockAllDragons && normalizedName.equals("com.projectedge.server.PlayerProfile")) {
                CtClass ctClass = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtMethod method = ctClass.getDeclaredMethod("hasDragonAccess");
                //method is programmed to set 'true'
                method.setBody("{ return true; }");
                System.out.println("[+] Injected! All available dragons unlocked.");
                return ctClass.toBytecode();
            }

            // BUG FIX: Omijanie crasha
            if (config.cheats.patchKnownBugs && normalizedName.equals("com.projectedge.server.NetworkHandler")) {
                CtClass ctClass = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtMethod method = ctClass.getDeclaredMethod("processCorruptedPacket");
                // Zastępujemy wadliwy kod pustą funkcją, by uniknąć crasha
                method.setBody("{ System.out.println(\"Crash prevention system active!\"); return; }");
                System.out.println("[+] Injected!");
                return ctClass.toBytecode();
            }

        } catch (Exception e) {
            //ignoring errors for now
        }
        return null;
    }
}