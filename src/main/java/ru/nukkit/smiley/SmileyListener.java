package ru.nukkit.smiley;


import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.server.ServerCommandEvent;

public class SmileyListener implements Listener {

	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onSmileChat (PlayerChatEvent event){
		if (!event.getPlayer().hasPermission("smiley.chat")) return;
		if (Smiley.getPlugin().colorsmiles) event.setMessage(Smileys.processSmiles(event.getMessage()));
		else event.setMessage(Smileys.processSmilesDecolor(event.getMessage()));
	}

	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onSmileCmd(PlayerCommandPreprocessEvent event){
		if (!Smiley.getPlugin().smilecmd) return;
		if (!event.getPlayer().hasPermission("smiley.commands")) return;
		if (Smiley.getPlugin().colorsmiles) event.setMessage(Smileys.processSmiles(event.getMessage()));
		else event.setMessage(Smileys.processSmilesDecolor(event.getMessage()));
	}

	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onRecodeConsoleCmd(ServerCommandEvent event) {
		if (!Smiley.getPlugin().smileconsole) return;
		if (Smiley.getPlugin().colorsmiles) event.setCommand(Smileys.processSmiles(event.getCommand()));
		else event.setCommand(Smileys.processSmilesDecolor(event.getCommand()));
	}

	/*
	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onJoin (PlayerJoinEvent event){
		Smiley.getPlugin().u.updateMsg(event.getPlayer());
	} */
	
	/*
	
	@EventHandler(priority=EventPriority.LOW, ignoreCancelled = true)
	public void onBookColorize(PlayerEditBookEvent event){
		if (!Smiley.getPlugin().bookcolorize) return;
		BookMeta bm = event.getNewBookMeta();
		if (bm.hasAuthor()) bm.setAuthor(Smileys.processSmiles(bm.getAuthor()));
		if (bm.hasTitle()) bm.setTitle(Smileys.processSmiles(bm.getTitle()));
		if (bm.hasPages()&&(bm.getPages().size()>0)) {
			List<String> pages  = new ArrayList<String>();
			for (int i = 0;i<bm.getPages().size(); i++)
				pages.add(i, ChatColor.translateAlternateColorCodes('&', bm.getPages().get(i)));
			bm.setPages(pages);
		}
		event.setNewBookMeta(bm);
	} */
	
	/*
	@EventHandler(priority=EventPriority.NORMAL)
	public void onReadSmileyBook (PlayerInteractEvent event){
		if (event.getAction()!=Action.RIGHT_CLICK_AIR&&event.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
		if (event.getItem()==null||event.getItem().getType()!=Material.WRITTEN_BOOK) return;
		ItemStack bookItem = event.getItem();
		if (!bookItem.hasItemMeta()) return;
		BookMeta book = (BookMeta) bookItem.getItemMeta();
		if (!book.hasDisplayName()||!book.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&5☺&3Smiley book&5☺"))) return;
		if (!book.hasAuthor()||!book.getAuthor().equals(ChatColor.translateAlternateColorCodes('&', "&6fromgate"))) return;
		if (!book.hasTitle()||!book.getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5☺&3Smiley book&5☺"))) return;
		book = Smileys.refillBookMeta (book);
		bookItem.setItemMeta(book);
		event.getPlayer().setItemInHand(bookItem);
		
	} */
	
	/*
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
	public void onBookSmiley (PlayerEditBookEvent event){
		if (!Smiley.getPlugin().smilebook) return;
		BookMeta bm = event.getNewBookMeta();
		if (bm.hasAuthor()) bm.setAuthor(Smileys.processSmiles(bm.getAuthor()));
		if (bm.hasTitle()) bm.setTitle(Smileys.processSmiles(bm.getTitle()));
		if (bm.hasPages()&&(bm.getPages().size()>0)) {
			List<String> pages  = new ArrayList<String>();
			for (int i = 0;i<bm.getPages().size(); i++)
				if (Smiley.getPlugin().bookcolor) pages.add(Smileys.processSmiles(bm.getPages().get(i),"&0"));
				else pages.add (Smileys.processSmilesDecolor(bm.getPages().get(i)));
			bm.setPages(pages);
		}
		event.setNewBookMeta(bm);
	} */
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onSignSmiley (SignChangeEvent event){
		if (!event.getPlayer().hasPermission("smiley.sign")) return;
		if (!Smiley.getPlugin().smilesign) return;
		for (int i = 0; i<4;i++)
			if (!event.getLine(i).isEmpty()) {
				if (Smiley.getPlugin().signcolor)	event.setLine(i, Smileys.processSmiles(event.getLine(i),"&0"));				
				else event.setLine(i, Smileys.processSmilesDecolor(event.getLine(i)));
			}
	}
	
	
	
}
