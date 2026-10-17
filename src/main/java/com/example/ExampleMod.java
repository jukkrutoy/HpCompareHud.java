package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class ExampleMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Регистрируем отрисовку нашего текста на экране (HUD)
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            
            // Проверяем, что игрок в мире и игра не на паузе
            if (client.player == null || client.world == null || client.options.hudHidden) {
                return;
            }

            // Проверяем, смотрит ли игрок на какое-либо существо
            HitResult hitResult = client.crosshairTarget;
            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHit = (EntityHitResult) hitResult;
                
                // Если цель является живым существом (игрок, моб)
                if (entityHit.getEntity() instanceof LivingEntity target) {
                    float playerHp = client.player.getHealth();
                    float targetHp = target.getHealth();

                    String text;
                    int color;

                    // Логика сравнения здоровья
                    if (playerHp > targetHp) {
                        text = "WIN";
                        color = 0x00FF00; // Зеленый цвет
                    } else if (playerHp < targetHp) {
                        text = "LOSE";
                        color = 0xFF0000; // Красный цвет
                    } else {
                        text = "EQUAL";
                        color = 0xFFFF00; // Желтый цвет, если ХП поровну
                    }

                    // Получаем ширину текста для центрирования
                    int textWidth = client.textRenderer.getWidth(text);
                    
                    // Координаты центра экрана
                    int x = drawContext.getScaledWindowWidth() / 2 - textWidth / 2;
                    int y = drawContext.getScaledWindowHeight() / 2 - 20; // Чуть выше прицела

                    // Рисуем текст с тенью
                    drawContext.drawTextWithShadow(client.textRenderer, text, x, y, color);
                }
            }
        });
    }
}
