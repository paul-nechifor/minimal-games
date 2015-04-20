package ro.minimul.mg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public abstract class GameMenu {
    private Item[] list;
    
    public static class Item {
        protected final String value;
        
        public Item(String value) {
            this.value = value;
        }
    }
    
    public static class StringItem extends Item {
        public int id;
        
        public StringItem(Context context, int id) {
            super(context.getString(id));
            this.id = id;
        }
    }
    
    public GameMenu() {
    }
    
    protected abstract Item[] createItems(Context context);
    protected abstract void onClick(int index, Item item);
    
    public AlertDialog buildAlertDialog(Context context) {
        list = createItems(context);
        
        final CharSequence[] items = getCharSequences();
        final GameMenu that = this;
        
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                that.onClick(which, list[which]);
            }
        });
        
        return builder.create();
    }
    
    private CharSequence[] getCharSequences() {
        CharSequence[] ret = new CharSequence[list.length];
        
        for (int i = 0; i < list.length; i++) {
            ret[i] = list[i].value;
        }
        
        return ret;
    }
}
