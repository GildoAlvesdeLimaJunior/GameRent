package GameRent;

public enum Plataforma{
    PC("Pc"),
    PS5("Playstation 5"),
    PS4("Playstation 4"),
    PS3("Playstation 3"),
    XBOX_S("Xbox Series S"),
    XBOX_X("Xbox Series X"),
    Xbox_ONE("Xbox One"),
    SWITCH_1("Nintendo Switch 1"),
    SWITCH_2("Nintendo Switch 2"),
    MOBILE("Celular"),
    VR("Realidade Virtual"),
    RETRO("Plataformas Retrô");


    private final String nomePlataforma;

    Plataforma(String nomePlataforma) {
        this.nomePlataforma = nomePlataforma;
    }

    public String getNomePlataforma(){
        return nomePlataforma;
    }
}
