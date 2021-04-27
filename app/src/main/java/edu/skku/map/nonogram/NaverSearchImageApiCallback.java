package edu.skku.map.nonogram;

@FunctionalInterface
interface NaverSearchImageApiCallback {
    void onSuccess(NaverImageResponse response);
}
