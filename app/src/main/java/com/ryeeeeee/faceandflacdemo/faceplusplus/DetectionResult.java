package com.ryeeeeee.faceandflacdemo.faceplusplus;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ryeeeeee on 1/13/16.
 */
public class DetectionResult {

    /**
     * face : [{"attribute":{"age":{"range":5,"value":23},"gender":{"confidence":99.9999,"value":"Female"},"glass":{"confidence":99.945,"value":"None"},"pose":{"pitch_angle":{"value":17},"roll_angle":{"value":0.735735},"yaw_angle":{"value":-2}},"race":{"confidence":99.6121,"value":"Asian"},"smiling":{"value":4.86501}},"face_id":"17233b4b1b51ac91e391e5afe130eb78","position":{"center":{"x":49.4,"y":37.6},"eye_left":{"x":43.3692,"y":30.8192},"eye_right":{"x":56.5606,"y":30.9886},"height":26.8,"mouth_left":{"x":46.1326,"y":44.9468},"mouth_right":{"x":54.2592,"y":44.6282},"nose":{"x":49.9404,"y":38.8484},"width":26.8},"tag":""}]
     * img_height : 500
     * img_id : 22fd9efc64c87e00224c33dd8718eec7
     * img_width : 500
     * session_id : 38047ad0f0b34c7e8c6efb6ba39ed355
     * url : http://www.faceplusplus.com.cn/wp-content/themes/faceplusplus/assets/img/demo/1.jpg?v=4
     */

    @SerializedName("img_height") public int imgHeight;
    @SerializedName("img_id") public String imgId;
    @SerializedName("img_width") public int imgWidth;
    @SerializedName("session_id") public String sessionId;
    @SerializedName("url") public String url;
    /**
     * attribute : {"age":{"range":5,"value":23},"gender":{"confidence":99.9999,"value":"Female"},"glass":{"confidence":99.945,"value":"None"},"pose":{"pitch_angle":{"value":17},"roll_angle":{"value":0.735735},"yaw_angle":{"value":-2}},"race":{"confidence":99.6121,"value":"Asian"},"smiling":{"value":4.86501}}
     * face_id : 17233b4b1b51ac91e391e5afe130eb78
     * position : {"center":{"x":49.4,"y":37.6},"eye_left":{"x":43.3692,"y":30.8192},"eye_right":{"x":56.5606,"y":30.9886},"height":26.8,"mouth_left":{"x":46.1326,"y":44.9468},"mouth_right":{"x":54.2592,"y":44.6282},"nose":{"x":49.9404,"y":38.8484},"width":26.8}
     * tag :
     */

    @SerializedName("face") public List<FaceEntity> face;

    public static DetectionResult objectFromData(String str) {

        return new Gson().fromJson(str, DetectionResult.class);
    }

    public static class FaceEntity {
        /**
         * age : {"range":5,"value":23}
         * gender : {"confidence":99.9999,"value":"Female"}
         * glass : {"confidence":99.945,"value":"None"}
         * pose : {"pitch_angle":{"value":17},"roll_angle":{"value":0.735735},"yaw_angle":{"value":-2}}
         * race : {"confidence":99.6121,"value":"Asian"}
         * smiling : {"value":4.86501}
         */

        @SerializedName("attribute") public AttributeEntity attribute;
        @SerializedName("face_id") public String faceId;
        /**
         * center : {"x":49.4,"y":37.6}
         * eye_left : {"x":43.3692,"y":30.8192}
         * eye_right : {"x":56.5606,"y":30.9886}
         * height : 26.8
         * mouth_left : {"x":46.1326,"y":44.9468}
         * mouth_right : {"x":54.2592,"y":44.6282}
         * nose : {"x":49.9404,"y":38.8484}
         * width : 26.8
         */

        @SerializedName("position") public PositionEntity position;
        @SerializedName("tag") public String tag;

        public static FaceEntity objectFromData(String str) {

            return new Gson().fromJson(str, FaceEntity.class);
        }

        public static class AttributeEntity {
            /**
             * range : 5
             * value : 23
             */

            @SerializedName("age") public AgeEntity age;
            /**
             * confidence : 99.9999
             * value : Female
             */

            @SerializedName("gender") public GenderEntity gender;
            /**
             * confidence : 99.945
             * value : None
             */

            @SerializedName("glass") public GlassEntity glass;
            /**
             * pitch_angle : {"value":17}
             * roll_angle : {"value":0.735735}
             * yaw_angle : {"value":-2}
             */

            @SerializedName("pose") public PoseEntity pose;
            /**
             * confidence : 99.6121
             * value : Asian
             */

            @SerializedName("race") public RaceEntity race;
            /**
             * value : 4.86501
             */

            @SerializedName("smiling") public SmilingEntity smiling;

            public static AttributeEntity objectFromData(String str) {

                return new Gson().fromJson(str, AttributeEntity.class);
            }

            public static class AgeEntity {
                @SerializedName("range") public int range;
                @SerializedName("value") public int value;

                public static AgeEntity objectFromData(String str) {

                    return new Gson().fromJson(str, AgeEntity.class);
                }
            }

            public static class GenderEntity {
                @SerializedName("confidence") public double confidence;
                @SerializedName("value") public String value;

                public static GenderEntity objectFromData(String str) {

                    return new Gson().fromJson(str, GenderEntity.class);
                }
            }

            public static class GlassEntity {
                @SerializedName("confidence") public double confidence;
                @SerializedName("value") public String value;

                public static GlassEntity objectFromData(String str) {

                    return new Gson().fromJson(str, GlassEntity.class);
                }
            }

            public static class PoseEntity {
                /**
                 * value : 17
                 */

                @SerializedName("pitch_angle") public PitchAngleEntity pitchAngle;
                /**
                 * value : 0.735735
                 */

                @SerializedName("roll_angle") public RollAngleEntity rollAngle;
                /**
                 * value : -2
                 */

                @SerializedName("yaw_angle") public YawAngleEntity yawAngle;

                public static PoseEntity objectFromData(String str) {

                    return new Gson().fromJson(str, PoseEntity.class);
                }

                public static class PitchAngleEntity {
                    @SerializedName("value") public int value;

                    public static PitchAngleEntity objectFromData(String str) {

                        return new Gson().fromJson(str, PitchAngleEntity.class);
                    }
                }

                public static class RollAngleEntity {
                    @SerializedName("value") public double value;

                    public static RollAngleEntity objectFromData(String str) {

                        return new Gson().fromJson(str, RollAngleEntity.class);
                    }
                }

                public static class YawAngleEntity {
                    @SerializedName("value") public int value;

                    public static YawAngleEntity objectFromData(String str) {

                        return new Gson().fromJson(str, YawAngleEntity.class);
                    }
                }
            }

            public static class RaceEntity {
                @SerializedName("confidence") public double confidence;
                @SerializedName("value") public String value;

                public static RaceEntity objectFromData(String str) {

                    return new Gson().fromJson(str, RaceEntity.class);
                }
            }

            public static class SmilingEntity {
                @SerializedName("value") public double value;

                public static SmilingEntity objectFromData(String str) {

                    return new Gson().fromJson(str, SmilingEntity.class);
                }
            }
        }

        public static class PositionEntity {
            /**
             * x : 49.4
             * y : 37.6
             */

            @SerializedName("center") public CenterEntity center;
            /**
             * x : 43.3692
             * y : 30.8192
             */

            @SerializedName("eye_left") public EyeLeftEntity eyeLeft;
            /**
             * x : 56.5606
             * y : 30.9886
             */

            @SerializedName("eye_right") public EyeRightEntity eyeRight;
            @SerializedName("height") public double height;
            /**
             * x : 46.1326
             * y : 44.9468
             */

            @SerializedName("mouth_left") public MouthLeftEntity mouthLeft;
            /**
             * x : 54.2592
             * y : 44.6282
             */

            @SerializedName("mouth_right") public MouthRightEntity mouthRight;
            /**
             * x : 49.9404
             * y : 38.8484
             */

            @SerializedName("nose") public NoseEntity nose;
            @SerializedName("width") public double width;

            public static PositionEntity objectFromData(String str) {

                return new Gson().fromJson(str, PositionEntity.class);
            }

            public static class CenterEntity {
                @SerializedName("x") public double x;
                @SerializedName("y") public double y;

                public static CenterEntity objectFromData(String str) {

                    return new Gson().fromJson(str, CenterEntity.class);
                }
            }

            public static class EyeLeftEntity {
                @SerializedName("x") public double x;
                @SerializedName("y") public double y;

                public static EyeLeftEntity objectFromData(String str) {

                    return new Gson().fromJson(str, EyeLeftEntity.class);
                }
            }

            public static class EyeRightEntity {
                @SerializedName("x") public double x;
                @SerializedName("y") public double y;

                public static EyeRightEntity objectFromData(String str) {

                    return new Gson().fromJson(str, EyeRightEntity.class);
                }
            }

            public static class MouthLeftEntity {
                @SerializedName("x") public double x;
                @SerializedName("y") public double y;

                public static MouthLeftEntity objectFromData(String str) {

                    return new Gson().fromJson(str, MouthLeftEntity.class);
                }
            }

            public static class MouthRightEntity {
                @SerializedName("x") public double x;
                @SerializedName("y") public double y;

                public static MouthRightEntity objectFromData(String str) {

                    return new Gson().fromJson(str, MouthRightEntity.class);
                }
            }

            public static class NoseEntity {
                @SerializedName("x") public double x;
                @SerializedName("y") public double y;

                public static NoseEntity objectFromData(String str) {

                    return new Gson().fromJson(str, NoseEntity.class);
                }
            }
        }
    }
}
