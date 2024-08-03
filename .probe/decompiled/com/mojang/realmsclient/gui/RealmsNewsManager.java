package com.mojang.realmsclient.gui;

import com.mojang.realmsclient.dto.RealmsNews;
import com.mojang.realmsclient.util.RealmsPersistence;

public class RealmsNewsManager {

    private final RealmsPersistence newsLocalStorage;

    private boolean hasUnreadNews;

    private String newsLink;

    public RealmsNewsManager(RealmsPersistence realmsPersistence0) {
        this.newsLocalStorage = realmsPersistence0;
        RealmsPersistence.RealmsPersistenceData $$1 = realmsPersistence0.read();
        this.hasUnreadNews = $$1.hasUnreadNews;
        this.newsLink = $$1.newsLink;
    }

    public boolean hasUnreadNews() {
        return this.hasUnreadNews;
    }

    public String newsLink() {
        return this.newsLink;
    }

    public void updateUnreadNews(RealmsNews realmsNews0) {
        RealmsPersistence.RealmsPersistenceData $$1 = this.updateNewsStorage(realmsNews0);
        this.hasUnreadNews = $$1.hasUnreadNews;
        this.newsLink = $$1.newsLink;
    }

    private RealmsPersistence.RealmsPersistenceData updateNewsStorage(RealmsNews realmsNews0) {
        RealmsPersistence.RealmsPersistenceData $$1 = new RealmsPersistence.RealmsPersistenceData();
        $$1.newsLink = realmsNews0.newsLink;
        RealmsPersistence.RealmsPersistenceData $$2 = this.newsLocalStorage.read();
        boolean $$3 = $$1.newsLink == null || $$1.newsLink.equals($$2.newsLink);
        if ($$3) {
            return $$2;
        } else {
            $$1.hasUnreadNews = true;
            this.newsLocalStorage.save($$1);
            return $$1;
        }
    }
}