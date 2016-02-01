package ru.nukkit.smiley;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class Smileys {
	private static Map<String,String> smiles = new HashMap<String,String>();
	
	public int getSmileIndex (String msg, String smilecode){
		return msg.indexOf(smilecode);
	}

	private static String processSmile (String msg, String smilecode, String defcolor){
		String str = msg;
		int i = str.indexOf(smilecode);
		if (i<0) return str;
		String c = TextFormat.getLastColors(str.substring(0, i));
		if (c.isEmpty()) c = defcolor;
		try {
			str = str.replaceFirst(Pattern.quote(smilecode), TextFormat.colorize(get(smilecode)+c));
		} catch (Exception e){
			str = str.replaceFirst(Pattern.quote(smilecode), "");
			Message.debugMessage("Failed to parse smiley:",smilecode);
		}
		return str;
	}

	public static String processSmiles (String msg){
		return processSmiles (msg,Smiley.getPlugin().default_color);
	}

	public static String processSmiles (String msg, String defcolor){
		String str = msg;
		for (String smilecode : smiles.keySet()){
			while (str.contains(smilecode))
				str = processSmile (str, smilecode,defcolor);
		}
		return str;
	}

	
	public static String processSmilesDecolor(String txt){
		String str = txt;
		for (String key : smiles.keySet())
			str = str.replace(key, TextFormat.clean(TextFormat.colorize(get(key))));
		return str;
	}

	public static void init(){
		smiles.clear();
		smiles.put(":(","&c☹");
		smiles.put(":-(","&c☹");
		smiles.put(":)","&6☺"); 
		smiles.put(":D","&6☻");
		smiles.put(":-)","&6☻");
		smiles.put(";\\","&6ツ");
		smiles.put(";)","&6㋡");
		smiles.put("<3","&4♡");
		smiles.put(":heart:","&4♥");
		smiles.put(":peace:","&e☮");  
		smiles.put(":death:","&4☠");  
		smiles.put(":nuke:","&6☢");   
		smiles.put(":bio:","&5☣");   
		smiles.put(":cccp:","&c☭");    
		smiles.put(":ship:","&3☸");
		smiles.put(":food:","&a♨");
		smiles.put(":star:","&e☀");   
		smiles.put(":cloud:","&b☁");   
		smiles.put(":umbrella:","&3☂");    
		smiles.put(":snowman:","&f☃");    
		smiles.put(":sun:","&6☼");  
		smiles.put(":moonr:","&a☽ ");
		smiles.put(":moon:","&a☾");    
		smiles.put(":rmb:","&6❖");
		smiles.put(":hat:","&3〠");
		smiles.put(":8:","&4♾");
		smiles.put(":star:","&c☆");
		smiles.put(":darkstar:","&4★");
		smiles.put(":*:","&4★");
		smiles.put(":+:","&c☆");
		smiles.put(":darkphone:","&6☎");
		smiles.put(":phone:","&4☏");
		smiles.put(":cup:","&5☕");
		smiles.put(":yinyang:","&1☯");
		smiles.put(":music:","&5♫♪♬♩♪");
		smiles.put(":flag:","&4⚐");
		smiles.put(":redflag:","&4⚑");
		load();
		save();
	}

	


	public static String get(String key){
		if (smiles.containsKey(key))
			return parseUTF8(smiles.get(key));
		return key;
	}

	public static void add(String key, String smile){
		smiles.put(key, smile);
	}

	public static boolean remove(String key){
		if (!smiles.containsKey(key)) return false;
		smiles.remove(key);
		return true;
	}

	public void clear(){
		smiles.clear();
	}

	public static void load(){
		try{
            Smiley.getPlugin().getDataFolder().mkdirs();
			File f = new File (Smiley.getPlugin().getDataFolder()+File.separator+"smiley.yml");
			if (!f.exists()) return;
			smiles.clear();
            Config cfg = new Config(f);
            for (String key : cfg.getAll().keySet())
                add (key, (String) cfg.getString(key,key));
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static String byteToHex(byte b) {
		// Returns hex String representation of byte b
		char hexDigit[] = {
				'0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
		};
		char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
		return new String(array);
	}

	public static String stringToHex(String str){
		if (str.isEmpty()) return "";
		String hstr = "";
		for (char c : str.toCharArray()){
			hstr =  hstr+(c <128 ? Character.toString(c) : "\\u"+Integer.toHexString(c));
		}
		return str;
	}

	public static void save(){
		try{
            Smiley.getPlugin().getDataFolder().mkdirs();
			File f = new File (Smiley.getPlugin().getDataFolder()+File.separator+"smiley.yml");
			if (!f.exists()) f.delete();
			f.createNewFile();
			if (smiles.isEmpty()) return;
			Config sml = new Config(f);
			for (String key : smiles.keySet())
				sml.set(key, Smiley.getPlugin().saveEncoded ? stringToHex(smiles.get(key)): smiles.get(key));
			sml.save();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/*
	 * Based on  loadConvert method of java.util.Properties
	 */
	public static String parseUTF8(String instr) {
		char [] in = instr.toCharArray();
		int len = in.length;
		int off = 0;
		char[] convtBuf = new char[len];
		char aChar;
		char[] out = convtBuf;
		int outLen = 0;
		int end = off + len;
		while (off < end) {
			aChar = in[off++];
			if (aChar == '\\') {
				aChar = in[off++];
				if(aChar == 'u') {
					// Read the xxxx
					int value=0;
					for (int i=0; i<4; i++) {
						aChar = in[off++];
						switch (aChar) {
						case '0': case '1': case '2': case '3': case '4':
						case '5': case '6': case '7': case '8': case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a': case 'b': case 'c':
						case 'd': case 'e': case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A': case 'B': case 'C':
						case 'D': case 'E': case 'F':

							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
						}
					}
					out[outLen++] = (char)value;
				} else {
					if (aChar == 't') aChar = '\t';
					else if (aChar == 'r') aChar = '\r';
					else if (aChar == 'n') aChar = '\n';
					else if (aChar == 'f') aChar = '\f';
					out[outLen++] = aChar;
				}
			} else {
				out[outLen++] = aChar;
			}
		}
		return new String (out, 0, outLen);
	}
	
	public static void printSmileyList(CommandSender sender, int page){
		List<String> sm = new ArrayList<String>();
		for (String key : smiles.keySet())
			sm.add("&e"+key+" : "+smiles.get(key));
		
		Paginator.printPage(sender, sm, Message.SMILEY_LIST.getText('6'), Message.SMILEY_LIST_FOOTER.getText('6'), Message.SMILEY_LIST_EMPTY.getText('c'), page, 0, false);
		
	}
	/*
	Оставим до момента ввода книг ;)


	public static void openBookInventory (Player player){
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&6Smiley book"));
		ItemStack book = new ItemStack (Material.WRITTEN_BOOK);
		BookMeta bm = (BookMeta) book.getItemMeta();
		bm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5в�є&3Smiley book&5в�є"));
		bm.setTitle(ChatColor.translateAlternateColorCodes('&', "&5в�є&3Smiley book&5в�є"));
		bm.setAuthor(ChatColor.translateAlternateColorCodes('&', "&6fromgate"));
		bm = refillBookMeta (bm);
		book.setItemMeta(bm);
		inv.setItem(4, book);
		player.openInventory(inv);
	} */

	/*
	public static BookMeta refillBookMeta(BookMeta book) {
		List<String> pages = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		sb.append(Smiley.getUtil().getMSG("msg_smileylist",'6')).append("\n");
		int count = 1;
		for (String key : smiles.keySet()){
			sb.append("&0").append(key).append(" - ").append(smiles.get(key)).append("\n");
			count++;
			if (count == 14) {
				pages.add(ChatColor.translateAlternateColorCodes('&', sb.toString()));
				sb = new StringBuilder();
				count = 0;
			}
		}
		if (!sb.toString().isEmpty()) pages.add(ChatColor.translateAlternateColorCodes('&', sb.toString())); 
		book.setPages(pages);
		return book;
	} */
	
}
