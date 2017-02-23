package com.mozz.remoteview.parser;

import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public final class SyntaxTree {

    private static final String TAG = SyntaxTree.class.getSimpleName();
    private static boolean DEBUG = false;

    private int mDepth;
    private SyntaxTree mParent;
    private int mIndex;
    List<SyntaxTree> mChildren;
    private String mNodeName;
    AttrsSet mAttrs;

    // for cache use
    int mBracketPair;
    int mTagPair;

    public SyntaxTree(String nodeName, SyntaxTree parent, int depth, int index) {
        mNodeName = nodeName;
        mDepth = depth;
        mParent = parent;
        mIndex = index;
        mChildren = new LinkedList<>();
        mAttrs = new AttrsSet();
    }

    public void addAttr(String attrName, String value) {
        mAttrs.put(attrName, value);
    }

    public void addAttr(String attrName, double value) {
        mAttrs.put(attrName, value);
    }

    public void addAttr(String attrName, int value) {
        mAttrs.put(attrName, value);
    }

    public SyntaxTree addChild(String nodeName, int index) {
        SyntaxTree child = new SyntaxTree(nodeName, this, this.mDepth + 1, index);
        if (DEBUG) {
            Log.d(TAG, "add child " + child.toString() + " to " + this.toString() + ".");
        }
        mChildren.add(child);
        return child;
    }

    public boolean isLeaf() {
        return mChildren.isEmpty();
    }

    public boolean isContainer() {
        return !isLeaf();
    }

    public int childrenCount() {
        return mChildren.size();
    }


    public void walkThrough(WalkAction action) {
        this.walkThroughInternal(action, mDepth);
    }

    private void walkThroughInternal(WalkAction action, int depth) {
        if (action != null)
            action.act(this, depth);

        Iterator<SyntaxTree> itr = mChildren.iterator();

        while (itr.hasNext()) {
            SyntaxTree child = itr.next();
            child.walkThroughInternal(action, this.mDepth + 1);
        }

    }

    public String getNodeName() {
        return mNodeName;
    }


    public String wholeTreeToString() {
        final StringBuilder sb = new StringBuilder();
        this.walkThrough(new WalkAction() {
            @Override
            public void act(SyntaxTree node, int depth) {
                for (int i = 0; i < depth; i++) {
                    sb.append("--");
                }
                sb.append(node);
                System.out.println(sb.toString());
            }
        });

        return sb.toString();
    }

    @Override
    public String toString() {
        String index = "@" + mIndex + ", ";
        return "[" + index + mNodeName + ", attrs=" + mAttrs.toString() + "]";
    }

    public SyntaxTree getParent() {
        return mParent;
    }

    public static void toggleDebug(boolean debug) {
        DEBUG = debug;
    }

    interface WalkAction {
        void act(SyntaxTree node, int depth);
    }
}