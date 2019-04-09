package org.feathercore.util.chat.component;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class TextComponent extends BaseComponent {

    private final String text;

    public TextComponent(String msg) {
        this.text = msg;
    }

    /**
     * Gets the text value of this TextComponent. TODO: what are
     * getUnformattedText and getUnformattedTextForChat missing that made
     * someone decide to create a third equivalent method that only
     * TextComponent can implement?
     */
    public String getChatComponentText_TextValue() {
        return this.text;
    }

    /**
     * Gets the text of this component, without any special formatting codes
     * added, for chat. TODO: why is this two different methods?
     */
    public String getUnformattedTextForChat() {
        return this.text;
    }

    /**
     * Creates a copy of this component. Almost a deep copy, except the style is
     * shallow-copied.
     */
    public TextComponent createCopy() {
        TextComponent chatcomponenttext = new TextComponent(this.text);
        chatcomponenttext.setChatStyle(this.getChatStyle().createShallowCopy());

        for (ChatComponent ichatcomponent : this.getSiblings()) {
            chatcomponenttext.appendSibling(ichatcomponent.createCopy());
        }

        return chatcomponenttext;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof TextComponent)) {
            return false;
        } else {
            TextComponent chatcomponenttext = (TextComponent) p_equals_1_;
            return this.text.equals(chatcomponenttext.getChatComponentText_TextValue()) && super.equals(p_equals_1_);
        }
    }

    public String toString() {
        return "TextComponent{text=\'" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}
