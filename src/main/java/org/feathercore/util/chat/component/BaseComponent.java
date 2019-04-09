package org.feathercore.util.chat.component;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.feathercore.util.chat.ChatStyle;
import org.feathercore.util.chat.EnumChatFormatting;

import java.util.Iterator;
import java.util.List;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public abstract class BaseComponent implements ChatComponent {

    protected List<ChatComponent> siblings = Lists.<ChatComponent>newArrayList();
    private ChatStyle style;

    /**
     * Appends the given component to the end of this one.
     */
    public ChatComponent appendSibling(ChatComponent component) {
        component.getChatStyle().setParentStyle(this.getChatStyle());
        this.siblings.add(component);
        return this;
    }

    public List<ChatComponent> getSiblings() {
        return this.siblings;
    }

    /**
     * Appends the given text to the end of this component.
     */
    public ChatComponent appendText(String text) {
        return this.appendSibling(new TextComponent(text));
    }

    public ChatComponent setChatStyle(ChatStyle style) {
        this.style = style;

        for (ChatComponent ichatcomponent : this.siblings) {
            ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
        }

        return this;
    }

    public ChatStyle getChatStyle() {
        if (this.style == null) {
            this.style = new ChatStyle();

            for (ChatComponent ichatcomponent : this.siblings) {
                ichatcomponent.getChatStyle().setParentStyle(this.style);
            }
        }

        return this.style;
    }

    public Iterator<ChatComponent> iterator() {
        return Iterators.<ChatComponent>concat(Iterators.<ChatComponent>forArray(new BaseComponent[]{this}), createDeepCopyIterator(this.siblings));
    }

    /**
     * Get the text of this component, <em>and all child components</em>, with
     * all special formatting codes removed.
     */
    public final String getUnformattedText() {
        StringBuilder stringbuilder = new StringBuilder();

        for (ChatComponent ichatcomponent : this) {
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
        }

        return stringbuilder.toString();
    }

    /**
     * Gets the text of this component, with formatting codes added for
     * rendering.
     */
    public final String getFormattedText() {
        StringBuilder stringbuilder = new StringBuilder();

        for (ChatComponent ichatcomponent : this) {
            stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
            stringbuilder.append(EnumChatFormatting.RESET);
        }

        return stringbuilder.toString();
    }

    public static Iterator<ChatComponent> createDeepCopyIterator(Iterable<ChatComponent> components) {
        Iterator<ChatComponent> iterator = Iterators.concat(Iterators.transform(components.iterator(), p_apply_1_ -> p_apply_1_.iterator()));
        iterator = Iterators.transform(iterator, p_apply_1_ -> {
            ChatComponent ichatcomponent = p_apply_1_.createCopy();
            ichatcomponent.setChatStyle(ichatcomponent.getChatStyle().createDeepCopy());
            return ichatcomponent;
        });
        return iterator;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof BaseComponent)) {
            return false;
        } else {
            BaseComponent chatcomponentstyle = (BaseComponent) p_equals_1_;
            return this.siblings.equals(chatcomponentstyle.siblings) && this.getChatStyle().equals(chatcomponentstyle.getChatStyle());
        }
    }

    public int hashCode() {
        return 31 * (this.style == null ? 0 : this.style.hashCode()) + (this.siblings == null ? 0 :  this.siblings.hashCode());
    }

    public String toString() {
        return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
    }
}
