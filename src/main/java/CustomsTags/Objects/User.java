package CustomsTags.Objects;

import CustomsTags.CustomsTagsPlugin;

import java.util.UUID;

public class User {

    private final CustomsTagsPlugin instance = CustomsTagsPlugin.getInstance();

    private final UUID uuid;
    private String activeTag;
    private int selectedPage;

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    public void register() {
        instance.getFileUtil().database.insert(uuid.toString(), "");
    }

    public void load() {
        setActiveTag(instance.getFileUtil().database.getTag(uuid.toString()));
    }

    public void setActiveTag(String tag) {
        activeTag = tag;
    }

    public String getActiveTag() {
        return activeTag;
    }

    public void setSelectedPage(int page){
        this.selectedPage = page;
    }

    public int getSelectedPage(){
        return selectedPage;
    }

}
