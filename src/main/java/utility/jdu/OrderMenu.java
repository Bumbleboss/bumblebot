package utility.jdu;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Menu;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.utils.Checks;
import net.dv8tion.jda.core.utils.PermissionUtil;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "unused"})
public class OrderMenu extends Menu {
	
	private final Color color;
    private static String footer;
    private final String text;
    private final String description;
    private final List<String> choices;
    private final BiConsumer<Message, Integer> action;
    private final Consumer<Message> cancel;
    private final boolean useLetters;
    private final boolean allowTypedInput;
    private final boolean useCancel;

	private final static String[] NUMBERS = new String[]{"1\u20E3","2\u20E3","3\u20E3",
	        "4\u20E3","5\u20E3","6\u20E3","7\u20E3","8\u20E3","9\u20E3", "\uD83D\uDD1F"};

	private final static String[] LETTERS = new String[]{"\uD83C\uDDE6","\uD83C\uDDE7","\uD83C\uDDE8",
	        "\uD83C\uDDE9","\uD83C\uDDEA","\uD83C\uDDEB","\uD83C\uDDEC","\uD83C\uDDED","\uD83C\uDDEE","\uD83C\uDDEF"};

	private final static String[] NUMBERSS = new String[]{"1","2","3","4","5","6","7","8","9", "10"};
	
	private final static String CANCEL = "\u274C";
	    
	@SuppressWarnings("static-access")
	protected OrderMenu(EventWaiter waiter, Set<User> users, Set<Role> roles, long timeout, TimeUnit unit,
            Color color, String text, String description, String footer, List<String> choices, BiConsumer<Message,Integer> action,
            Consumer<Message> cancel, boolean useLetters, boolean allowTypedInput, boolean useCancel) {
		super(waiter, users, roles, timeout, unit);
		this.color = color;
	    this.text = text;
        this.footer = footer;
	    this.description = description;
	    this.choices = choices;
	    this.action = action;
	    this.cancel = cancel;
	    this.useLetters = useLetters;
	    this.allowTypedInput = allowTypedInput;
	    this.useCancel = useCancel;
	}

    @Override
    public void display(MessageChannel channel){
        if(channel.getType()==ChannelType.TEXT
                && !allowTypedInput
                && !PermissionUtil.checkPermission((TextChannel)channel,
                ((TextChannel)channel).getGuild().getSelfMember(), Permission.MESSAGE_ADD_REACTION))
            throw new PermissionException("Must be able to add reactions if not allowing typed input!");
        initialize(channel.sendMessage(getMessage()));
    }
    
    @Override
    public void display(Message message){
        if(message.getChannelType() == ChannelType.TEXT
                && !allowTypedInput 
                && !PermissionUtil.checkPermission(message.getTextChannel(),
                message.getGuild().getSelfMember(), Permission.MESSAGE_ADD_REACTION))
            throw new PermissionException("Must be able to add reactions if not allowing typed input!");
        initialize(message.editMessage(getMessage()));
    }
    private void initialize(RestAction<Message> ra){
    	ra.queue(m -> {
            try{
                for(int i=1; i<=choices.size(); i++){	
                    if(!(i<choices.size())) {
                    	ra.queue(v -> {
                    		if(allowTypedInput) {
                    			waitGeneric(m);
                    		}else {
                    			waitReactionOnly(m);
                    		}
                    	});
                    }
                }
            }catch(PermissionException ex){
            	if(allowTypedInput) {
            		waitGeneric(m);
            	}else {
            		waitReactionOnly(m);
            	}
            }
        });
     }

    private void waitGeneric(Message m){
        waiter.waitForEvent(GenericMessageEvent.class, e -> {
            if(e instanceof MessageReactionAddEvent)
                return isValidReaction(m, (MessageReactionAddEvent)e);
            if(e instanceof MessageReceivedEvent)
                return isValidMessage(m, (MessageReceivedEvent)e);
            return false;
        }, e -> {
            m.delete().queue();
            if(e instanceof MessageReactionAddEvent){
                MessageReactionAddEvent event = (MessageReactionAddEvent)e;
                if(event.getReaction().getReactionEmote().getName().equals(CANCEL)) {
                    cancel.accept(m);
                }else{
                    action.accept(m, getNumber(event.getReaction().getReactionEmote().getName()));
                }
            }
            else if (e instanceof MessageReceivedEvent) {
                MessageReceivedEvent event = (MessageReceivedEvent)e;
                int num = getMessageNumber(event.getMessage().getContentRaw());
                if(num<0 || num>choices.size())
                    cancel.accept(m);
                else
                    action.accept(m, num);
            }
        }, timeout, unit, () -> cancel.accept(m));
    }

    private void waitReactionOnly(Message m){
        waiter.waitForEvent(MessageReactionAddEvent.class, e -> isValidReaction(m, e), e -> {
            m.delete().queue();
            if(e.getReaction().getReactionEmote().getName().equals(CANCEL))
                cancel.accept(m);
            else
                action.accept(m, getNumber(e.getReaction().getReactionEmote().getName()));
        }, timeout, unit, () -> cancel.accept(m));
    }

    private Message getMessage() {
        MessageBuilder mbuilder = new MessageBuilder();
        if(text!=null)
            mbuilder.append(text);
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<choices.size(); i++) {
        	sb.append("\n**").append(getEmoji(i)).append(".** ").append(choices.get(i));
        	String foot;
        	if(footer==null) {
        		foot = "Choose from 1 - " + (i+1);
        	}else {
        		foot = "Choose from 1 - " + (i+1) + " to pick " + footer;       	
        	}
        	mbuilder.setEmbed(new EmbedBuilder().setColor(color)
        			.setFooter(foot + " | Timeout is " + timeout + " " + unit.toString().toLowerCase(), null)
        			.setDescription(description==null ? sb.toString() : description+sb.toString()).build());
        }
        return mbuilder.build();
    }
    
    private boolean isValidReaction(Message m, MessageReactionAddEvent e){
        if(!e.getMessageId().equals(m.getId()))
            return false;
        if(!isValidUser(e.getUser(), e.getGuild()))
            return false;
        if(e.getReaction().getReactionEmote().getName().equals(CANCEL))
            return true;

        int num = getNumber(e.getReaction().getReactionEmote().getName());
        return !(num<0 || num>choices.size());
    }
    
    private boolean isValidMessage(Message m, MessageReceivedEvent e){
        if(!e.getChannel().equals(m.getChannel()))
            return false;
        return isValidUser(e.getAuthor(), e.getGuild());
    }
    
    private String getEmoji(int number){
        return useLetters ? LETTERS[number] : NUMBERSS[number];
    }
    
    private int getNumber(String emoji){
        String[] array = useLetters ? LETTERS : NUMBERS;
        for(int i=0; i<array.length; i++)
            if(array[i].equals(emoji))
                return i+1;
        return -1;
    }
    
    private int getMessageNumber(String message){
        if(useLetters)
            return message.length()==1 ? " abcdefghij".indexOf(message.toLowerCase()) : -1;
        else {
            if(message.length()==1)
                return " 123456789".indexOf(message);
            return message.equals("10") ? 10 : -1;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class Builder extends Menu.Builder<Builder, OrderMenu>{
        private Color color;
        private String text;
        private String description;
        private final List<String> choices = new LinkedList<>();
        private BiConsumer<Message, Integer> selection;
        private Consumer<Message> cancel = (m) -> {};
        private boolean useLetters = false;
        private boolean allowTypedInput = true;
        private boolean addCancel = false;

        @Override
        public OrderMenu build(){
            Checks.check(waiter != null, "Must set an EventWaiter");
            Checks.check(!choices.isEmpty(), "Must have at least one choice");
            Checks.check(choices.size() <= 10, "Must have no more than ten choices");
            Checks.check(selection != null, "Must provide an selection consumer");
            Checks.check(text != null || description != null, "Either text or description must be set");
            return new OrderMenu(waiter,users,roles,timeout,unit,color,text,description,footer,choices,
                selection,cancel,useLetters,allowTypedInput,addCancel);
        }

        public Builder setColor(Color color){
            this.color = color;
            return this;
        }

        public Builder useLetters(){
            this.useLetters = true;
            return this;
        }

        public Builder useNumbers(){
            this.useLetters = false;
            return this;
        }

        public Builder allowTextInput(boolean allow){
            this.allowTypedInput = allow;
            return this;
        }

        public Builder useCancelButton(boolean use){
            this.addCancel = use;
            return this;
        }

        public Builder setText(String text){
            this.text = text;
            return this;
        }

        public Builder setDescription(String description){
            this.description = description;
            return this;
        }

        public Builder setSelection(BiConsumer<Message, Integer> selection){
            this.selection = selection;
            return this;
        }

        public Builder setCancel(Consumer<Message> cancel){
            this.cancel = cancel;
            return this;
        }

        public Builder addChoice(String choice){
            Checks.check(choices.size() < 10, "Cannot set more than 10 choices");
            this.choices.add(choice);
            return this;
        }
        public Builder addChoices(String... choices){
            for(String choice : choices)
                addChoice(choice);
            return this;
        }
        
        public Builder setChoices(String... choices) {
            clearChoices();
            return addChoices(choices);
        }

        public Builder clearChoices() {
            this.choices.clear();
            return this;
        }
    }
}