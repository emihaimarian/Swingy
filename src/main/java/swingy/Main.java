package swingy;

import swingy.controller.common.*;
import swingy.controller.adventure.*;
import swingy.controller.continuea.*;
import swingy.controller.loot.ActionGetNewItem;
import swingy.controller.loot.ActionKeepMyStuff;
import swingy.controller.mainmenu.*;
import swingy.controller.newhero.*;

import swingy.model.Enemy;
import swingy.model.GameMap;
import swingy.model.Player;
import swingy.view.ConsoleView;
import swingy.view.GuiView;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Main {
    private static InputStreamReader reader = new InputStreamReader(System.in);
    private static BufferedReader in = new BufferedReader(reader);

    public static void main(String[] args) {
        //java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        //magical - do not touch
        @SuppressWarnings("unused")
        org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger("org.hibernate");
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        if (args.length == 0 || (args.length == 1 && (args[0].equals("gui") || args[0].equals("console")))) {
            GuiView guiView = new GuiView();
            ConsoleView consoleView = new ConsoleView();
            Player player = new Player();
            GameMap map = new GameMap();
            Controller controller = new Controller(guiView, consoleView, player, map);

            guiView.setMainMenu_listeners(new ActionNewHero(controller, guiView), new ActionContinueA(controller, guiView), new ActionRunAndHide(controller, guiView));
            guiView.setNewHero_listeners(new ActionSelectClass(controller, guiView), new ActionBack(controller, guiView), new ActionToAdventure(controller, guiView));
            guiView.setContinueA_listeners(new ActionSelectHero(controller, guiView), new ActionExecuteHero(controller, guiView));
            guiView.setAdventure_listeners(
                    new ActionConsole(controller, guiView),
                    new ActionSave(controller, guiView),
                    new ActionMoveN(controller, guiView), new ActionMoveW(controller, guiView), new ActionMoveS(controller, guiView), new ActionMoveE(controller, guiView),
                    new ActionRun(controller, guiView),
                    new ActionFight(controller, guiView));
            guiView.setLoot_Actions(new ActionKeepMyStuff(controller, guiView), new ActionGetNewItem(controller, guiView));
            guiView.buildGui();

            if (args.length == 1 && args[0].equals("console"))
                controller.console();

            controller.start(); // [???] Useless [???]
        }
        else {
            System.out.println("Valid parameters: [NONE], gui, console");
        }
    }
}
