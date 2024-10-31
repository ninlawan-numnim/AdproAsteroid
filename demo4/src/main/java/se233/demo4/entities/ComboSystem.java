// ComboSystem.java
package se233.demo4.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ComboSystem {
    private int comboCount;
    private long lastHitTime;
    private static final long COMBO_TIMEOUT = 2000; // 2 seconds
    private List<ComboEffect> activeEffects = new ArrayList<>();

    public void registerHit() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHitTime < COMBO_TIMEOUT) {
            comboCount++;
            if (comboCount % 5 == 0) { // ทุกๆ 5 combo
                activateComboEffect();
            }
        } else {
            comboCount = 1;
        }
        lastHitTime = currentTime;
    }

    private void activateComboEffect() {
        ComboEffect effect = new ComboEffect(comboCount);
        activeEffects.add(effect);
    }

    public void update() {
        Iterator<ComboEffect> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            ComboEffect effect = iterator.next();
            if (effect.isExpired()) {
                iterator.remove();
            }
        }
    }

    public void drawComboCounter(GraphicsContext gc) {
        if (comboCount > 1) {
            gc.setFill(Color.ORANGE);
            gc.setFont(Font.font(20));
            gc.fillText(comboCount + "x COMBO!", 350, 50);

            // วาด special effects
            for (ComboEffect effect : activeEffects) {
                effect.draw(gc);
            }
        }
    }
}

