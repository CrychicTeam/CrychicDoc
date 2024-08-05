package com.mojang.realmsclient.gui.screens;

import javax.annotation.Nullable;

public class UploadResult {

    public final int statusCode;

    @Nullable
    public final String errorMessage;

    UploadResult(int int0, String string1) {
        this.statusCode = int0;
        this.errorMessage = string1;
    }

    public static class Builder {

        private int statusCode = -1;

        private String errorMessage;

        public UploadResult.Builder withStatusCode(int int0) {
            this.statusCode = int0;
            return this;
        }

        public UploadResult.Builder withErrorMessage(@Nullable String string0) {
            this.errorMessage = string0;
            return this;
        }

        public UploadResult build() {
            return new UploadResult(this.statusCode, this.errorMessage);
        }
    }
}