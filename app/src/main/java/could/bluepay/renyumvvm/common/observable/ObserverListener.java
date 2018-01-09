package could.bluepay.renyumvvm.common.observable;


import java.util.List;

import could.bluepay.renyumvvm.common.entity.LocalMedia;
import could.bluepay.renyumvvm.common.entity.LocalMediaFolder;

/**
 *  观察者
 */
public interface ObserverListener {
    void observerUpFoldersData(List<LocalMediaFolder> folders);

    void observerUpSelectsData(List<LocalMedia> selectMedias);
}
