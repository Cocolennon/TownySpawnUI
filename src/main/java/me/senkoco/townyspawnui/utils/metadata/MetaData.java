package me.senkoco.townyspawnui.utils.metadata;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

public class MetaData {
    public static StringDataField blockInMenu = new StringDataField("townyspawnui_blockinmenu");

    public static String getBlockInMenu(Nation nation) {
        return MetaDataUtil.getString(nation, blockInMenu);
    }

    public static String getBlockInMenu(Town town){
        return MetaDataUtil.getString(town, blockInMenu);
    }

    public static void setBlockInMenu(Nation nation, String blockName){
        if(!nation.hasMeta("townyspawnui_blockinmenu")){
            MetaDataUtil.addNewStringMeta(nation, "townyspawnui_blockinmenu", blockName, true);
            return;
        }
        MetaDataUtil.setString(nation, blockInMenu, blockName, true);
    }

    public static void setBlockInMenu(Town town, String blockName){
        if(!town.hasMeta("townyspawnui_blockinmenu")){
            MetaDataUtil.addNewStringMeta(town, "townyspawnui_blockinmenu", blockName, true);
            return;
        }
        MetaDataUtil.setString(town, blockInMenu, blockName, true);
    }
}
