package com.github.maxos.legacyBookshelves.config.impl;

import com.github.maxos.legacyBookshelves.config.Config;
import com.github.maxos.legacyBookshelves.config.data.ParamData;
import com.github.maxos.legacyBookshelves.file.FileManager;
import com.github.maxos.legacyBookshelves.utils.Colorizer;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

public class ParamConfig extends Config {

    // ключи от секции
    private static final String MAIN_SECTION = "parameters";
    //private static final String CAN_PUT_SIMPLE_KEY = "can-put-simple";
    //private static final String CAN_STACK_KEY = "can-stack";
    private static final String FREQUENCY_SAVE_DB_KEY = "frequency-save-db";
    //private static final String INVENTORY_TITLE_KEY = "inventory-title";
    //private static final String INVENTORY_SIZE_KEY = "inventory-size";

    // дефолтные значения
    //private static final boolean CAN_PUT_SIMPLE_DEFAULT_VALUE = true;
    //private static final boolean CAN_STACK_DEFAULT_VALUE = true;
    private static final int FREQUENCY_SAVE_DB_DEFAULT_VALUE = 30;
    //private static final String INVENTORY_TITLE_DEFAULT_VALUE = Colorizer.colorize("&0▶ КНИЖНАЯ ПОЛКА");
    //private static final int INVENTORY_SIZE_DEFAULT_VALUE = 36;

    public ParamConfig(FileManager file) {
        super(file);
    }

    @Getter
    private ParamData paramData;

    @Override
    protected void parseConfig() {
        ConfigurationSection paramSection = getSection(MAIN_SECTION);
        if (paramSection == null) return;

        //boolean canPutSimple = paramSection.getBoolean(CAN_PUT_SIMPLE_KEY);
        //boolean canStack = paramSection.getBoolean(CAN_STACK_KEY);
        //String title = paramSection.getString(INVENTORY_TITLE_KEY);
        //int size = paramSection.getInt(INVENTORY_SIZE_KEY);
        int frequencySaveDb = paramSection.getInt(FREQUENCY_SAVE_DB_KEY);

        paramData = new ParamData(
                frequencySaveDb
        );

    }

    @Override
    protected void setDefaultValues() {
        paramData = new ParamData(
                //CAN_PUT_SIMPLE_DEFAULT_VALUE,
                //CAN_STACK_DEFAULT_VALUE,
                FREQUENCY_SAVE_DB_DEFAULT_VALUE
                //INVENTORY_TITLE_DEFAULT_VALUE,
                //INVENTORY_SIZE_DEFAULT_VALUE
        );
    }

}
