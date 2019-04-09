package org.feathercore.util.chat.component;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class SelectorComponent extends BaseComponent {

    /**
     * The selector used to find the matching entities of this text component
     */
    private final String selector;

    public SelectorComponent(String selectorIn) {
        this.selector = selectorIn;
    }

    /**
     * Gets the selector of this component, in plain text.
     */
    public String getSelector() {
        return this.selector;
    }

    /**
     * Gets the text of this component, without any special formatting codes
     * added, for chat. TODO: why is this two different methods?
     */
    public String getUnformattedTextForChat() {
        return this.selector;
    }

    /**
     * Creates a copy of this component. Almost a deep copy, except the style is
     * shallow-copied.
     */
    public SelectorComponent createCopy() {
        SelectorComponent chatcomponentselector = new SelectorComponent(this.selector);
        chatcomponentselector.setChatStyle(this.getChatStyle().createShallowCopy());

        for (ChatComponent ichatcomponent : this.getSiblings()) {
            chatcomponentselector.appendSibling(ichatcomponent.createCopy());
        }

        return chatcomponentselector;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof SelectorComponent)) {
            return false;
        } else {
            SelectorComponent chatcomponentselector = (SelectorComponent) p_equals_1_;
            return this.selector.equals(chatcomponentselector.selector) && super.equals(p_equals_1_);
        }
    }

    public String toString() {
        return "SelectorComponent{pattern=\'" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}
