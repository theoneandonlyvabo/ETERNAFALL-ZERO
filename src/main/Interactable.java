package main;

public interface Interactable {

    float  getInteractRadius();
    int    getWorldX();
    int    getWorldY();
    String getPromptText();
    void   interact();

    /** Return path ke icon NPC, atau null kalau nggak punya. */
    default String getIconPath() { return null; }

}