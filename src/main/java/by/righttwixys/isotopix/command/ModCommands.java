package by.righttwixys.isotopix.command;

import by.righttwixys.isotopix.init.ModAttachments;
import by.righttwixys.isotopix.radiation.EntityRadiation;
import by.righttwixys.isotopix.radiation.RadiationSimulationHandler;
import by.righttwixys.isotopix.radiation.RayVisualMode;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("isotopix")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("debug")
                        .then(Commands.literal("rays")
                                .executes(ctx -> {
                                    RayVisualMode current = RadiationSimulationHandler.getVisualMode();
                                    RayVisualMode next;
                                    switch (current) {
                                        case OFF -> next = RayVisualMode.TRAILS;
                                        case TRAILS -> next = RayVisualMode.OFF;
                                        default -> next = RayVisualMode.OFF;
                                    }
                                    RadiationSimulationHandler.setVisualMode(next);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§aРежим отображения радиации: §e" + next.name()), true);
                                    return 1;
                                })
                                .then(Commands.literal("off").executes(ctx -> {
                                    RadiationSimulationHandler.setVisualMode(RayVisualMode.OFF);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§aОтображение радиации: §cВЫКЛЮЧЕНО"), true);
                                    return 1;
                                }))
                                .then(Commands.literal("trails").executes(ctx -> {
                                    RadiationSimulationHandler.setVisualMode(RayVisualMode.TRAILS);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§aОтображение радиации(лучи/нити)"), true);
                                    return 1;
                                }))

                        )
                )
                .then(Commands.literal("radiation")
                        .then(Commands.literal("get")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(ctx -> {
                                            ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
                                            EntityRadiation rad = target.getData(ModAttachments.RADIATION);
                                            ctx.getSource().sendSuccess(() -> Component.literal(String.format("§eИгрок %s: Поглощено: %.4f Р, Поле: %.1f мкР/ч (%d CPS)",
                                                    target.getName().getString(), rad.getAccumulatedDoseRoentgen(), rad.getCurrentDoseRateMicroRPerHour(), rad.getCountsPerSecond())), false);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("set")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("roentgen", FloatArgumentType.floatArg(0.0f))
                                                .executes(ctx -> {
                                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                                    float doseR = FloatArgumentType.getFloat(ctx, "roentgen");
                                                    for (ServerPlayer player : targets) {
                                                        player.getData(ModAttachments.RADIATION).setAccumulatedDoseRoentgen(doseR);
                                                    }
                                                    ctx.getSource().sendSuccess(() -> Component.literal("§aУстановлена доза " + doseR + " Р для " + targets.size() + " игроков"), true);
                                                    return targets.size();
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("clear")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes(ctx -> {
                                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                            for (ServerPlayer player : targets) {
                                                player.getData(ModAttachments.RADIATION).setAccumulatedDoseRoentgen(0.0f);
                                                player.getData(ModAttachments.RADIATION).setCurrentDoseRateMicroRPerHour(0.0f);
                                            }
                                            ctx.getSource().sendSuccess(() -> Component.literal("§aРадиация очищена для " + targets.size() + " игроков"), true);
                                            return targets.size();
                                        })
                                )
                        )
                )
        );
    }
}