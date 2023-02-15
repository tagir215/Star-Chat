#version 100

uniform mat4 mVPMatrix;
attribute vec4 vPosition;
attribute vec4 aColor;
varying vec4 vColor;

void main() {
    gl_Position = mVPMatrix * vPosition;
    vColor = aColor;
}

