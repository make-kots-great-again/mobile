package com.morgan.make_kots_great_again;

public class List {

    private String list_id;
    private String list_name;
    private String list_type;
    private boolean is_selected = false;

    /**
     * --------------
     *  CONSTRUCTOR
     * --------------
     * @param id
     * @param name
     */
    public List(String id, String name, String type) {
        this.list_id = id;
        this.list_name = name;
        this.list_type = type;
    }

    public String getList_id() {
        return list_id;
    }

    public String getList_name() {
        return list_name;
    }

    public boolean is_list_personal() {
        if (list_type.equals("PERSONAL")){ return true; }
        return false;
    }

    public boolean get_selected_list() {
        return is_selected;
    }

    public void set_is_selected (boolean is_selected) {
        this.is_selected = is_selected;
    }
}
