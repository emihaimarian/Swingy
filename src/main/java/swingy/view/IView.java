package swingy.view;

import swingy.common.GamePhase;
import swingy.controller.common.Controller;

public interface IView {
    void registerController(Controller controller);
    void showGamePhase(GamePhase gamePhase, String mapData, String messData);
    void setActive(boolean status, String mapData, String messData);
    void setNewPlayerData(String name, int classIndex, String heroInfo);
    void updateHeroList(String[] heroListArr);
    void updateSelectedHero(String heroInfo);
    String getHeroNameValue();
}
