#version 100

uniform mat4 mVPMatrix;
attribute vec4 vPosition;
attribute vec2 aTextureCoordinates;
varying vec2 vTextureCoordinates;

void main() {
    gl_Position = mVPMatrix * vPosition;
    vTextureCoordinates = aTextureCoordinates;
}
