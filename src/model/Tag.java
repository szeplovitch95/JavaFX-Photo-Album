package model;

import java.io.Serializable;

/**
 * @author Shachar Zeplovitch
 * @author Christopher McDonough
 */
public class Tag implements Serializable {
    private String tagType;
    private String tagValue;

    /**
     * @param tagType
     * @param tagValue
     */
    public Tag(String tagType, String tagValue) {
	this.tagType = tagType;
	this.tagValue = tagValue;
    }

    /**
     * @return tagType String
     */
    public String getTagType() {
	return tagType;
    }

    /**
     * @param tagType String
     * sets the tagType to the string argument.
     */
    public void setTagType(String tagType) {
	this.tagType = tagType;
    }

    /**
     * @return tagValue String
     */
    public String getTagValue() {
	return tagValue;
    }

    /**
     * @param tagValue String
     * sets the tagValue to the string argument.
     */
    public void setTagValue(String tagValue) {
	this.tagValue = tagValue;
    }

    /**
     * @param t Tag
     * @return true if the tag equals to the other tag 
     * returns false if they are not equal.
     */
    public boolean equals(Tag t) {
	if (t == null) {
	    return false;
	}

	return t.getTagType().equals(this.tagType) && t.getTagValue().equals(this.tagValue);
    }
}