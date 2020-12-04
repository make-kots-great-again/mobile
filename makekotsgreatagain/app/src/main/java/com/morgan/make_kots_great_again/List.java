package com.morgan.make_kots_great_again;

public class List {

    private String list_id;
    private String list_name;
    protected enum ListType {GROUP, PERSONAL}
    private boolean is_selected = false;

    private ListType listType;

    /**
     * --------------
     *  CONSTRUCTOR
     * --------------
     * @param id
     * @param name
     */
    public List(String id, String name, ListType type) {
        this.list_id = id;
        this.list_name = name;
        this.listType = type;
    }

    public String getList_id() {
        return list_id;
    }

    public String getList_name() {
        return list_name;
    }
    public ListType getListType() {
        return listType;
    }
    public boolean get_selected_list() {
        return is_selected;
    }

    public void set_is_selected (boolean is_selected) {
        this.is_selected = is_selected;
    }
}
