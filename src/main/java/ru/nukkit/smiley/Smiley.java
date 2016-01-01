package ru.nukkit.smiley;



import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.io.File;
import java.io.IOException;

public class Smiley extends PluginBase {
	private static Smiley plugin;

	// Конфигурация
	String default_color = "&f";
	boolean colorsmiles = true;
	boolean smileconsole = true;
	boolean smilecmd = true;
	boolean smilesign = true;
	boolean signcolor = false;
	//boolean smilebook = true;
	//boolean bookcolor = true;
	//boolean bookcolorize = true;
	boolean saveEncoded = true;

	@Override
	public void onEnable() {
		plugin = this;
		initCfg();
		loadCfg();
		saveCfg();
		Smileys.init();
		Message.init(this);
		getServer().getPluginManager().registerEvents(new SmileyListener (), this);
	}

	private void initCfg(){
		this.getDataFolder().mkdirs();
		File f = new File(this.getDataFolder(),"config.yml");
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
			}
	}

	public void loadCfg(){
		Config cfg = this.getConfig();
		smileconsole = cfg.getNested("smiley.console",true);
		smilecmd = cfg.getNested("smiley.commands",true);
		colorsmiles = cfg.getNested("smiley.colors",true); 
		smilesign = cfg.getNested("smiley.sign.enable-smileys",true);
		signcolor = cfg.getNested("smiley.sign.colored-smileys",false);
		//smilebook = cfg.getNested("smiley.book.enable-smileys",true);
		//bookcolor = cfg.getNested("smiley.book.colored-smileys",true);
        //bookcolorize = cfg.getNested("smiley.book.colorize-text",true);
		default_color = cfg.getNested("smiley.default-chat-color","&f");
	}

	private void saveCfg(){
		Config cfg = this.getConfig();
		cfg.setNested("smiley.console",smileconsole);
		cfg.setNested("smiley.commands",smilecmd);
		cfg.setNested("smiley.colors",colorsmiles);
		cfg.setNested("smiley.sign.enable-smileys",smilesign);
		cfg.setNested("smiley.sign.colored-smileys",signcolor);
		//cfg.setNested("smiley.book.enable-smileys",smilebook);;
        //cfg.setNested("smiley.book.colored-smileys",bookcolor);;
        //cfg.setNested("smiley.book.colorize-text",bookcolorize);;
		cfg.setNested("smiley.default-chat-color",default_color );
		this.saveConfig();
	}
	public static Smiley getPlugin() {
		return plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd1, String cmdLabel, String[] args) {
		Player player = (sender instanceof Player) ? (Player) sender : null;
		if (args.length == 0){
			return false;
		} else if ((args.length>=1)&&(args[0].equalsIgnoreCase("list"))){
			if ((!sender.hasPermission("smiley.list"))&&player!=null) return false;
			int pnum = 1;
			if ((args.length==2)&&(args[1].matches("\\d+"))) pnum = Integer.parseInt(args[1]);
			Smileys.printSmileyList(sender,pnum);
		} else if ((args.length==1)&&(args[0].equalsIgnoreCase("reload"))){
			if ((!sender.hasPermission("smiley.config"))&&player!=null) return false;
			loadCfg();
			Smileys.load();
			Message.RELOADED.print(sender);
		} else return false;
		return true;
	}
}
