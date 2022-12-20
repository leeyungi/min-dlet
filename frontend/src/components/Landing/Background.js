import React, { Suspense } from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls } from '@react-three/drei';
import Dandelion from './Dandelion';

const dandles = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15];

function FallbackTitle() {
  return null;
}

export default function Background() {
  return (
    <Canvas
      frameloop="demand"
      style={{ pointerEvents: 'auto' }}
      camera={{
        position: [0, -80, 120],
        // <Canvas frameloop="demand" style={{pointerEvents: 'auto', cursor:'pointer'}} pixelRatio={[1, 1]} camera={{ position: [-15, 27, 150],
        fov: 90,
        far: 500,
        near: 10,
      }}
    >
      <ambientLight intensity={0.3} />
      <Suspense fallback={<FallbackTitle />}>
        <directionalLight
          position={[0.5, 1, 0.866]}
          intensity={1.7}
          // castShadow
        />

        <directionalLight
          // color="#b6fccd"
          position={[0.5, 10, 0.866]}
          intensity={1.7}
          // castShadow
        />

        {dandles.map((dandle) => {
          return <Dandelion key={dandle} seed={dandle} />;
        })}
        <directionalLight
          position={[-1, -0.3, -0.866]}
          intensity={1.5}
          // castShadow
        />
      </Suspense>
      <OrbitControls
        enableRotate={false}
        enablePan={false}
        autoRotate={true}
        autoRotateSpeed={0.5}
        minDistance={120}
        maxDistance={150}
      />
    </Canvas>
  );
}
