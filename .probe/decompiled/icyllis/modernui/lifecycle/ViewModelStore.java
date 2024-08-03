package icyllis.modernui.lifecycle;

import java.util.HashMap;

public class ViewModelStore {

    private final HashMap<String, ViewModel> mMap = new HashMap();

    final void put(String key, ViewModel viewModel) {
        ViewModel oldViewModel = (ViewModel) this.mMap.put(key, viewModel);
        if (oldViewModel != null) {
            oldViewModel.onCleared();
        }
    }

    final ViewModel get(String key) {
        return (ViewModel) this.mMap.get(key);
    }

    public final void clear() {
        for (ViewModel vm : this.mMap.values()) {
            vm.clear();
        }
        this.mMap.clear();
    }
}