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
     * @return
     */
    public String getTagType() {
	return tagType;
    }

    /**
     * @param tagType
     */
    public void setTagType(String tagType) {
	this.tagType = tagType;
    }

    /**
     * @return
     */
    public String getTagValue() {
	return tagValue;
    }

    /**
     * @param tagValue
     */
    public void setTagValue(String tagValue) {
	this.tagValue = tagValue;
    }

    /**
     * @param t
     * @return
     */
    public boolean equals(Tag t) {
	if (t == null) {
	    return false;
	}

	return t.getTagType().equals(this.tagType) && t.getTagValue().equals(this.tagValue);
    }
}