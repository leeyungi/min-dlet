import React, { useEffect, useState } from 'react';
import { useGLTF, useAnimations } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';

export default function FlyingSeedAnimation({
  seedUp,
  wind,
  ready,
  appear,
  touch,
}) {
  const [size, SetSize] = useState(8);
  const [state, SetState] = useState(false);
  const [down, SetDown] = useState(false);
  const [up, SetUp] = useState(false);
  const { nodes, animations, materials } = useGLTF(
    require('assets/Models/flyingSeed.glb')
  );
  const { ref, actions, names } = useAnimations(animations);

  useEffect(() => {}, [actions, names]);
  useEffect(() => {
    if (seedUp) SetUp(true);
  }, [seedUp]);
  useEffect(() => {
    ref.current.rotation.y = 2.2;
    ref.current.position.x = 0;
    ref.current.position.y = -150;
    ref.current.position.z = 0;
  }, []);
  useFrame(() => {
    if (up && wind && ref.current.position.y > -150) {
      SetSize(size - 0.001);
      ref.current.position.y -= 0.3;
    } else if (up && ref.current.position.y < -65) {
      SetSize(size + 0.001);
      ref.current.position.y += 2;
    } else if (up && ref.current.position.y >= -65) {
      SetSize(size + 0.0001);
      ready(true);
    } else if (up && wind && ref.current.position.y <= -150) {
      appear(true);
    } else {
      SetSize(size + 0.0001);
    }
  });

  useEffect(() => {
    if (wind) {
      let i = 0;
      for (i = 0; i < 57; i++) {
        actions[names[i]].play();
        //Todo: 무조건 로딩 안보이게
      }
    }
  }, [wind]);

  const eventClick = () => {
    console.log('터치 이벤트');
    touch(true);
  };

  return (
    <group ref={ref} dispose={null} onClick={eventClick}>
      <group name="Scene">
        <group name="Dandelion" rotation={[Math.PI / 2, 0, 0.84]} scale={size}>
          <group name="ani_seed">
            <group
              name="Dandelion_seed0135"
              position={[0.1, 0, 0.04]}
              rotation={[Math.PI, 0.68, 0]}
              scale={[-1, -1, -0.98]}
            >
              <group name="polySurface1050" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh103"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh103.geometry}
                  material={nodes.Mesh103.material}
                />
                <mesh
                  name="Mesh103_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh103_1.geometry}
                  material={nodes.Mesh103_1.material}
                />
              </group>
              <mesh
                name="polySurface3050"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3050.geometry}
                material={nodes.polySurface3050.material}
                position={[0, 0, 2.92]}
                rotation={[0, 0, -0.35]}
                scale={[1.08, 1.08, 2.01]}
              />
            </group>
            <group
              name="Dandelion_seed0136"
              position={[0.11, 0, 0.05]}
              rotation={[Math.PI, 0.91, 0]}
              scale={[-1, -1, -0.96]}
            >
              <group name="polySurface1" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh003"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh003.geometry}
                  material={nodes.Mesh003.material}
                />
                <mesh
                  name="Mesh003_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh003_1.geometry}
                  material={nodes.Mesh003_1.material}
                />
              </group>
              <mesh
                name="polySurface3"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3.geometry}
                material={nodes.polySurface3.material}
                position={[0, 0, 3.68]}
                rotation={[0, 0, 0.71]}
                scale={[1.08, 1.08, 2.26]}
              />
            </group>
            <group
              name="Dandelion_seed0137"
              position={[0.11, 0, 0.05]}
              rotation={[Math.PI, 1.14, 0]}
              scale={[-1, -1, -1.01]}
            >
              <group name="polySurface1001" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh005"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh005.geometry}
                  material={nodes.Mesh005.material}
                />
                <mesh
                  name="Mesh005_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh005_1.geometry}
                  material={nodes.Mesh005_1.material}
                />
              </group>
              <mesh
                name="polySurface3001"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3001.geometry}
                material={nodes.polySurface3001.material}
                position={[0, 0, 1.17]}
                rotation={[0, 0, 0.13]}
                scale={[1.08, 1.08, 1.4]}
              />
            </group>
            <group
              name="Dandelion_seed0138"
              position={[0.1, 0, 0.05]}
              rotation={[Math.PI, 1.37, 0]}
              scale={[-1, -1, -0.93]}
            >
              <group name="polySurface1002" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh007"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh007.geometry}
                  material={nodes.Mesh007.material}
                />
                <mesh
                  name="Mesh007_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh007_1.geometry}
                  material={nodes.Mesh007_1.material}
                />
              </group>
              <mesh
                name="polySurface3002"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3002.geometry}
                material={nodes.polySurface3002.material}
                position={[0, 0, 0.24]}
                rotation={[0, 0, 0.85]}
                scale={1.08}
              />
            </group>
            <group
              name="Dandelion_seed0139"
              position={[0.11, 0, 0.04]}
              rotation={[0, 1.55, Math.PI]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1028" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh059"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh059.geometry}
                  material={nodes.Mesh059.material}
                />
                <mesh
                  name="Mesh059_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh059_1.geometry}
                  material={nodes.Mesh059_1.material}
                />
              </group>
              <mesh
                name="polySurface3028"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3028.geometry}
                material={nodes.polySurface3028.material}
                position={[0, 0, 0.24]}
                rotation={[0, 0, -0.46]}
                scale={1.08}
              />
            </group>
            <group
              name="Dandelion_seed0140"
              position={[0.08, 0, 0.01]}
              rotation={[0, 1.32, -Math.PI]}
              scale={[-1, -1, -0.94]}
            >
              <group name="polySurface1029" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh061"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh061.geometry}
                  material={nodes.Mesh061.material}
                />
                <mesh
                  name="Mesh061_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh061_1.geometry}
                  material={nodes.Mesh061_1.material}
                />
              </group>
              <mesh
                name="polySurface3029"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3029.geometry}
                material={nodes.polySurface3029.material}
                position={[0, 0, 0.24]}
                rotation={[0, 0, 0.37]}
                scale={1.08}
              />
            </group>
            <group
              name="Dandelion_seed0141"
              position={[0.06, 0, -0.05]}
              rotation={[0, 1.06, Math.PI]}
              scale={[-1, -1, -1.02]}
            >
              <group name="polySurface1037" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh077"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh077.geometry}
                  material={nodes.Mesh077.material}
                />
                <mesh
                  name="Mesh077_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh077_1.geometry}
                  material={nodes.Mesh077_1.material}
                />
              </group>
              <mesh
                name="polySurface3037"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3037.geometry}
                material={nodes.polySurface3037.material}
                position={[0, 0, 0.24]}
                rotation={[0, 0, 0.48]}
                scale={1.08}
              />
            </group>
            <group
              name="Dandelion_seed0152"
              position={[0.09, -0.05, 0.04]}
              rotation={[2.75, 0.65, 0.2]}
              scale={-1}
            >
              <group name="polySurface1051" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh105"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh105.geometry}
                  material={nodes.Mesh105.material}
                />
                <mesh
                  name="Mesh105_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh105_1.geometry}
                  material={nodes.Mesh105_1.material}
                />
              </group>
              <mesh
                name="polySurface3051"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3051.geometry}
                material={nodes.polySurface3051.material}
                position={[0, 0, 1.72]}
                scale={[1.08, 1.08, 1.59]}
              />
            </group>
            <group
              name="Dandelion_seed0153"
              position={[0.12, -0.04, 0.06]}
              rotation={[2.7, 0.85, 0.29]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1003" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh009"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh009.geometry}
                  material={nodes.Mesh009.material}
                />
                <mesh
                  name="Mesh009_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh009_1.geometry}
                  material={nodes.Mesh009_1.material}
                />
              </group>
              <mesh
                name="polySurface3003"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3003.geometry}
                material={nodes.polySurface3003.material}
                position={[0, 0, 2.72]}
                scale={[1.08, 1.08, 1.94]}
              />
            </group>
            <group
              name="Dandelion_seed0154"
              position={[0.1, -0.05, 0.1]}
              rotation={[2.45, 0.98, 0.56]}
              scale={-1}
            >
              <group name="polySurface1004" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh011"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh011.geometry}
                  material={nodes.Mesh011.material}
                />
                <mesh
                  name="Mesh011_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh011_1.geometry}
                  material={nodes.Mesh011_1.material}
                />
              </group>
              <mesh
                name="polySurface3004"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3004.geometry}
                material={nodes.polySurface3004.material}
                position={[0, 0, 0.98]}
                scale={[1.08, 1.08, 1.34]}
              />
            </group>
            <group
              name="Dandelion_seed0155"
              position={[0.09, -0.02, 0.13]}
              rotation={[2.34, 1.2, 0.72]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1005" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh013"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh013.geometry}
                  material={nodes.Mesh013.material}
                />
                <mesh
                  name="Mesh013_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh013_1.geometry}
                  material={nodes.Mesh013_1.material}
                />
              </group>
              <mesh
                name="polySurface3005"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3005.geometry}
                material={nodes.polySurface3005.material}
                position={[0, 0, 2.02]}
                scale={[1.08, 1.08, 1.69]}
              />
            </group>
            <group
              name="Dandelion_seed0156"
              position={[0.05, 0.01, 0.15]}
              rotation={[1.34, -1.27, -1.85]}
            >
              <group name="polySurface1017" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh037"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh037.geometry}
                  material={nodes.Mesh037.material}
                />
                <mesh
                  name="Mesh037_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh037_1.geometry}
                  material={nodes.Mesh037_1.material}
                />
              </group>
              <mesh
                name="polySurface3017"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3017.geometry}
                material={nodes.polySurface3017.material}
                position={[0, 0, 0.24]}
                scale={1.08}
              />
            </group>
            <group
              name="Dandelion_seed0157"
              position={[0.04, 0, -0.04]}
              rotation={[2.41, -1.09, -0.71]}
            >
              <group name="polySurface1035" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh073"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh073.geometry}
                  material={nodes.Mesh073.material}
                />
                <mesh
                  name="Mesh073_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh073_1.geometry}
                  material={nodes.Mesh073_1.material}
                />
              </group>
              <mesh
                name="polySurface3035"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3035.geometry}
                material={nodes.polySurface3035.material}
                position={[0, 0, 0.24]}
                scale={1.08}
              />
            </group>
            <group
              name="Dandelion_seed0159"
              position={[0.07, -0.08, 0.04]}
              rotation={[2.52, 0.45, 0.14]}
              scale={-1}
            >
              <group name="polySurface1052" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh107"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh107.geometry}
                  material={nodes.Mesh107.material}
                />
                <mesh
                  name="Mesh107_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh107_1.geometry}
                  material={nodes.Mesh107_1.material}
                />
              </group>
              <mesh
                name="polySurface3052"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3052.geometry}
                material={nodes.polySurface3052.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0160"
              position={[0.1, -0.08, 0.06]}
              rotation={[2.42, 0.66, 0.34]}
              scale={-1}
            >
              <group name="polySurface1006" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh015"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh015.geometry}
                  material={nodes.Mesh015.material}
                />
                <mesh
                  name="Mesh015_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh015_1.geometry}
                  material={nodes.Mesh015_1.material}
                />
              </group>
              <mesh
                name="polySurface3006"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3006.geometry}
                material={nodes.polySurface3006.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0161"
              position={[0.08, -0.05, 0.12]}
              rotation={[2.17, 0.89, 0.68]}
              scale={-1}
            >
              <group name="polySurface1007" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh017"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh017.geometry}
                  material={nodes.Mesh017.material}
                />
                <mesh
                  name="Mesh017_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh017_1.geometry}
                  material={nodes.Mesh017_1.material}
                />
              </group>
              <mesh
                name="polySurface3007"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3007.geometry}
                material={nodes.polySurface3007.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0162"
              position={[0.06, -0.04, 0.12]}
              rotation={[1.75, 1.01, 1.2]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1008" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh019"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh019.geometry}
                  material={nodes.Mesh019.material}
                />
                <mesh
                  name="Mesh019_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh019_1.geometry}
                  material={nodes.Mesh019_1.material}
                />
              </group>
              <mesh
                name="polySurface3008"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3008.geometry}
                material={nodes.polySurface3008.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0163"
              position={[0.03, -0.02, 0.07]}
              rotation={[1.29, 1, 1.74]}
              scale={-1}
            >
              <group name="polySurface1031" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh065"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh065.geometry}
                  material={nodes.Mesh065.material}
                />
                <mesh
                  name="Mesh065_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh065_1.geometry}
                  material={nodes.Mesh065_1.material}
                />
              </group>
              <mesh
                name="polySurface3031"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3031.geometry}
                material={nodes.polySurface3031.material}
                position={[0, 0, 0.48]}
                scale={[0.92, 0.92, 1.17]}
              />
            </group>
            <group
              name="Dandelion_seed0164"
              position={[0.04, 0.01, -0.02]}
              rotation={[0.86, 0.81, 2.28]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1032" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh067"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh067.geometry}
                  material={nodes.Mesh067.material}
                />
                <mesh
                  name="Mesh067_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh067_1.geometry}
                  material={nodes.Mesh067_1.material}
                />
              </group>
              <mesh
                name="polySurface3032"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3032.geometry}
                material={nodes.polySurface3032.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0165"
              position={[0.03, -0.02, -0.04]}
              rotation={[1.16, 0.68, 1.98]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1033" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh069"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh069.geometry}
                  material={nodes.Mesh069.material}
                />
                <mesh
                  name="Mesh069_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh069_1.geometry}
                  material={nodes.Mesh069_1.material}
                />
              </group>
              <mesh
                name="polySurface3033"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3033.geometry}
                material={nodes.polySurface3033.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0166"
              position={[0.01, -0.05, 0.04]}
              rotation={[1.63, 0.78, 1.28]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1009" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh021"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh021.geometry}
                  material={nodes.Mesh021.material}
                />
                <mesh
                  name="Mesh021_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh021_1.geometry}
                  material={nodes.Mesh021_1.material}
                />
              </group>
              <mesh
                name="polySurface3009"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3009.geometry}
                material={nodes.polySurface3009.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0167"
              position={[0.02, -0.07, 0.08]}
              rotation={[1.98, 0.68, 0.76]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1010" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh023"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh023.geometry}
                  material={nodes.Mesh023.material}
                />
                <mesh
                  name="Mesh023_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh023_1.geometry}
                  material={nodes.Mesh023_1.material}
                />
              </group>
              <mesh
                name="polySurface3010"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3010.geometry}
                material={nodes.polySurface3010.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0168"
              position={[0.03, -0.08, 0.09]}
              rotation={[2.22, 0.46, 0.33]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1011" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh025"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh025.geometry}
                  material={nodes.Mesh025.material}
                />
                <mesh
                  name="Mesh025_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh025_1.geometry}
                  material={nodes.Mesh025_1.material}
                />
              </group>
              <mesh
                name="polySurface3011"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3011.geometry}
                material={nodes.polySurface3011.material}
                position={[0, 0, 0.48]}
                scale={[0.92, 0.92, 1.17]}
              />
            </group>
            <group
              name="Dandelion_seed0169"
              position={[0.04, -0.11, 0.05]}
              rotation={[2.31, 0.26, 0.07]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1053" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh109"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh109.geometry}
                  material={nodes.Mesh109.material}
                />
                <mesh
                  name="Mesh109_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh109_1.geometry}
                  material={nodes.Mesh109_1.material}
                />
              </group>
              <mesh
                name="polySurface3053"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3053.geometry}
                material={nodes.polySurface3053.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0170"
              position={[0.05, 0.05, -0.04]}
              rotation={[0.98, 0.55, 2.28]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1034" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh071"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh071.geometry}
                  material={nodes.Mesh071.material}
                />
                <mesh
                  name="Mesh071_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh071_1.geometry}
                  material={nodes.Mesh071_1.material}
                />
              </group>
              <mesh
                name="polySurface3034"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3034.geometry}
                material={nodes.polySurface3034.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0171"
              position={[0.06, 0.01, -0.1]}
              rotation={[1.03, 0.28, 2.25]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1039" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh081"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh081.geometry}
                  material={nodes.Mesh081.material}
                />
                <mesh
                  name="Mesh081_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh081_1.geometry}
                  material={nodes.Mesh081_1.material}
                />
              </group>
              <mesh
                name="polySurface3039"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3039.geometry}
                material={nodes.polySurface3039.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0172"
              position={[0.04, -0.13, 0.08]}
              rotation={[2.08, 0.24, 0.13]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1023" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh049"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh049.geometry}
                  material={nodes.Mesh049.material}
                />
                <mesh
                  name="Mesh049_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh049_1.geometry}
                  material={nodes.Mesh049_1.material}
                />
              </group>
              <mesh
                name="polySurface3023"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3023.geometry}
                material={nodes.polySurface3023.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0173"
              position={[0.01, -0.12, 0.13]}
              rotation={[1.84, 0.31, 0.48]}
              scale={-1}
            >
              <group name="polySurface1024" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh051"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh051.geometry}
                  material={nodes.Mesh051.material}
                />
                <mesh
                  name="Mesh051_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh051_1.geometry}
                  material={nodes.Mesh051_1.material}
                />
              </group>
              <mesh
                name="polySurface3024"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3024.geometry}
                material={nodes.polySurface3024.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0174"
              position={[0, -0.09, 0.1]}
              rotation={[1.79, 0.51, 0.87]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1012" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh027"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh027.geometry}
                  material={nodes.Mesh027.material}
                />
                <mesh
                  name="Mesh027_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh027_1.geometry}
                  material={nodes.Mesh027_1.material}
                />
              </group>
              <mesh
                name="polySurface3012"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3012.geometry}
                material={nodes.polySurface3012.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0175"
              position={[-0.02, -0.07, 0.04]}
              rotation={[1.56, 0.55, 1.33]}
              scale={-1}
            >
              <group name="polySurface1013" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh029"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh029.geometry}
                  material={nodes.Mesh029.material}
                />
                <mesh
                  name="Mesh029_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh029_1.geometry}
                  material={nodes.Mesh029_1.material}
                />
              </group>
              <mesh
                name="polySurface3013"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3013.geometry}
                material={nodes.polySurface3013.material}
                position={[0, 0, 0.48]}
                scale={[0.92, 0.92, 1.17]}
              />
            </group>
            <group
              name="Dandelion_seed0176"
              position={[0, -0.04, -0.05]}
              rotation={[1.26, 0.47, 1.92]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1040" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh083"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh083.geometry}
                  material={nodes.Mesh083.material}
                />
                <mesh
                  name="Mesh083_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh083_1.geometry}
                  material={nodes.Mesh083_1.material}
                />
              </group>
              <mesh
                name="polySurface3040"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3040.geometry}
                material={nodes.polySurface3040.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0177"
              position={[-0.04, -0.04, -0.03]}
              rotation={[1.14, 0.2, 1.99]}
              scale={-1}
            >
              <group name="polySurface1041" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh085"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh085.geometry}
                  material={nodes.Mesh085.material}
                />
                <mesh
                  name="Mesh085_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh085_1.geometry}
                  material={nodes.Mesh085_1.material}
                />
              </group>
              <mesh
                name="polySurface3041"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3041.geometry}
                material={nodes.polySurface3041.material}
                position={[0, 0, 0.48]}
                scale={[0.92, 0.92, 1.17]}
              />
            </group>
            <group
              name="Dandelion_seed0178"
              position={[-0.03, -0.05, -0.06]}
              rotation={[1.34, 0.26, 1.87]}
              scale={-1}
            >
              <group name="polySurface1042" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh087"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh087.geometry}
                  material={nodes.Mesh087.material}
                />
                <mesh
                  name="Mesh087_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh087_1.geometry}
                  material={nodes.Mesh087_1.material}
                />
              </group>
              <mesh
                name="polySurface3042"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3042.geometry}
                material={nodes.polySurface3042.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0179"
              position={[-0.03, -0.06, -0.08]}
              rotation={[1.52, 0.27, 1.77]}
              scale={-1}
            >
              <group name="polySurface1025" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh053"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh053.geometry}
                  material={nodes.Mesh053.material}
                />
                <mesh
                  name="Mesh053_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh053_1.geometry}
                  material={nodes.Mesh053_1.material}
                />
              </group>
              <mesh
                name="polySurface3025"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3025.geometry}
                material={nodes.polySurface3025.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0180"
              position={[0.07, -0.06, -0.12]}
              rotation={[0, 0.84, -Math.PI]}
              scale={-1}
            >
              <group name="polySurface1046" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh095"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh095.geometry}
                  material={nodes.Mesh095.material}
                />
                <mesh
                  name="Mesh095_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh095_1.geometry}
                  material={nodes.Mesh095_1.material}
                />
              </group>
              <mesh
                name="polySurface3046"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3046.geometry}
                material={nodes.polySurface3046.material}
                position={[0, 0, 0.24]}
                rotation={[0, 0, 0.48]}
                scale={1.08}
              />
            </group>
            <group
              name="Dandelion_seed0181"
              position={[0.08, 0.02, -0.12]}
              rotation={[0.54, 0.72, 2.49]}
              scale={-1}
            >
              <group name="polySurface1047" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh097"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh097.geometry}
                  material={nodes.Mesh097.material}
                />
                <mesh
                  name="Mesh097_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh097_1.geometry}
                  material={nodes.Mesh097_1.material}
                />
              </group>
              <mesh
                name="polySurface3047"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3047.geometry}
                material={nodes.polySurface3047.material}
                position={[0, 0, 0.48]}
                scale={[0.92, 0.92, 1.17]}
              />
            </group>
            <group
              name="Dandelion_seed0182"
              position={[0.08, 0.06, -0.11]}
              rotation={[0.74, 0.45, 2.39]}
              scale={-1}
            >
              <group name="polySurface1049" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh101"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh101.geometry}
                  material={nodes.Mesh101.material}
                />
                <mesh
                  name="Mesh101_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh101_1.geometry}
                  material={nodes.Mesh101_1.material}
                />
              </group>
              <mesh
                name="polySurface3049"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3049.geometry}
                material={nodes.polySurface3049.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0184"
              position={[0.12, -0.01, -0.16]}
              rotation={[2.64, -0.71, -0.6]}
            >
              <group name="polySurface1048" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh099"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh099.geometry}
                  material={nodes.Mesh099.material}
                />
                <mesh
                  name="Mesh099_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh099_1.geometry}
                  material={nodes.Mesh099_1.material}
                />
              </group>
              <mesh
                name="polySurface3048"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3048.geometry}
                material={nodes.polySurface3048.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0188"
              position={[0.12, 0.04, 0.06]}
              rotation={[0.44, -0.85, -2.85]}
            >
              <group name="polySurface1014" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh031"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh031.geometry}
                  material={nodes.Mesh031.material}
                />
                <mesh
                  name="Mesh031_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh031_1.geometry}
                  material={nodes.Mesh031_1.material}
                />
              </group>
              <mesh
                name="polySurface3014"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3014.geometry}
                material={nodes.polySurface3014.material}
                position={[0, 0, 0.24]}
                scale={1.08}
              />
            </group>
            <group
              name="Dandelion_seed0189"
              position={[0.1, 0.05, 0.1]}
              rotation={[0.7, -0.98, -2.58]}
            >
              <group name="polySurface1015" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh033"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh033.geometry}
                  material={nodes.Mesh033.material}
                />
                <mesh
                  name="Mesh033_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh033_1.geometry}
                  material={nodes.Mesh033_1.material}
                />
              </group>
              <mesh
                name="polySurface3015"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3015.geometry}
                material={nodes.polySurface3015.material}
                position={[0, 0, 1.12]}
                scale={[1.08, 1.08, 1.39]}
              />
            </group>
            <group
              name="Dandelion_seed0190"
              position={[0.09, 0.02, 0.13]}
              rotation={[0.8, -1.2, -2.42]}
            >
              <group name="polySurface1016" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh035"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh035.geometry}
                  material={nodes.Mesh035.material}
                />
                <mesh
                  name="Mesh035_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh035_1.geometry}
                  material={nodes.Mesh035_1.material}
                />
              </group>
              <mesh
                name="polySurface3016"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3016.geometry}
                material={nodes.polySurface3016.material}
                position={[0, 0, 0.24]}
                scale={1.08}
              />
            </group>
            <group
              name="Dandelion_seed0191"
              position={[0.1, 0.08, 0.06]}
              rotation={[0.73, -0.66, -2.81]}
            >
              <group name="polySurface1018" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh039"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh039.geometry}
                  material={nodes.Mesh039.material}
                />
                <mesh
                  name="Mesh039_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh039_1.geometry}
                  material={nodes.Mesh039_1.material}
                />
              </group>
              <mesh
                name="polySurface3018"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3018.geometry}
                material={nodes.polySurface3018.material}
                position={[0, 0, 0.42]}
                scale={[0.92, 0.92, 1.14]}
              />
            </group>
            <group
              name="Dandelion_seed0192"
              position={[0.08, 0.05, 0.12]}
              rotation={[0.97, -0.89, -2.46]}
            >
              <group name="polySurface1019" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh041"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh041.geometry}
                  material={nodes.Mesh041.material}
                />
                <mesh
                  name="Mesh041_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh041_1.geometry}
                  material={nodes.Mesh041_1.material}
                />
              </group>
              <mesh
                name="polySurface3019"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3019.geometry}
                material={nodes.polySurface3019.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0193"
              position={[0.06, 0.04, 0.12]}
              rotation={[1.39, -1.01, -1.95]}
            >
              <group name="polySurface1020" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh043"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh043.geometry}
                  material={nodes.Mesh043.material}
                />
                <mesh
                  name="Mesh043_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh043_1.geometry}
                  material={nodes.Mesh043_1.material}
                />
              </group>
              <mesh
                name="polySurface3020"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3020.geometry}
                material={nodes.polySurface3020.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0194"
              position={[0.01, 0.05, 0.04]}
              rotation={[1.51, -0.78, -1.86]}
            >
              <group name="polySurface1021" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh045"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh045.geometry}
                  material={nodes.Mesh045.material}
                />
                <mesh
                  name="Mesh045_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh045_1.geometry}
                  material={nodes.Mesh045_1.material}
                />
              </group>
              <mesh
                name="polySurface3021"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3021.geometry}
                material={nodes.polySurface3021.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0195"
              position={[0.02, 0.07, 0.08]}
              rotation={[1.16, -0.68, -2.38]}
            >
              <group name="polySurface1022" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh047"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh047.geometry}
                  material={nodes.Mesh047.material}
                />
                <mesh
                  name="Mesh047_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh047_1.geometry}
                  material={nodes.Mesh047_1.material}
                />
              </group>
              <mesh
                name="polySurface3022"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3022.geometry}
                material={nodes.polySurface3022.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0196"
              position={[0, 0.09, 0.1]}
              rotation={[1.35, -0.51, -2.27]}
            >
              <group name="polySurface1026" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh055"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh055.geometry}
                  material={nodes.Mesh055.material}
                />
                <mesh
                  name="Mesh055_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh055_1.geometry}
                  material={nodes.Mesh055_1.material}
                />
              </group>
              <mesh
                name="polySurface3026"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3026.geometry}
                material={nodes.polySurface3026.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0197"
              position={[-0.02, 0.07, 0.04]}
              rotation={[1.59, -0.55, -1.81]}
            >
              <group name="polySurface1027" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh057"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh057.geometry}
                  material={nodes.Mesh057.material}
                />
                <mesh
                  name="Mesh057_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh057_1.geometry}
                  material={nodes.Mesh057_1.material}
                />
              </group>
              <mesh
                name="polySurface3027"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3027.geometry}
                material={nodes.polySurface3027.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0198"
              position={[0.05, -0.01, 0.15]}
              rotation={[1.8, 1.27, 1.29]}
              scale={-1}
            >
              <group name="polySurface1030" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh063"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh063.geometry}
                  material={nodes.Mesh063.material}
                />
                <mesh
                  name="Mesh063_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh063_1.geometry}
                  material={nodes.Mesh063_1.material}
                />
              </group>
              <mesh
                name="polySurface3030"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3030.geometry}
                material={nodes.polySurface3030.material}
                position={[0, 0, 1.01]}
                scale={[1.08, 1.08, 1.35]}
              />
            </group>
            <group
              name="Dandelion_seed0199"
              position={[0.03, 0.02, 0.07]}
              rotation={[1.85, -1, -1.4]}
            >
              <group name="polySurface1036" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh075"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh075.geometry}
                  material={nodes.Mesh075.material}
                />
                <mesh
                  name="Mesh075_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh075_1.geometry}
                  material={nodes.Mesh075_1.material}
                />
              </group>
              <mesh
                name="polySurface3036"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3036.geometry}
                material={nodes.polySurface3036.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0200"
              position={[0.04, 0, -0.04]}
              rotation={[0.73, 1.09, 2.43]}
              scale={[-1, -1, -1]}
            >
              <group name="polySurface1038" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh079"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh079.geometry}
                  material={nodes.Mesh079.material}
                />
                <mesh
                  name="Mesh079_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh079_1.geometry}
                  material={nodes.Mesh079_1.material}
                />
              </group>
              <mesh
                name="polySurface3038"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3038.geometry}
                material={nodes.polySurface3038.material}
                position={[0, 0, 0.24]}
                scale={1.08}
              />
            </group>
            <group
              name="Dandelion_seed0201"
              position={[0.04, -0.01, -0.02]}
              rotation={[2.29, -0.81, -0.86]}
            >
              <group name="polySurface1043" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh089"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh089.geometry}
                  material={nodes.Mesh089.material}
                />
                <mesh
                  name="Mesh089_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh089_1.geometry}
                  material={nodes.Mesh089_1.material}
                />
              </group>
              <mesh
                name="polySurface3043"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3043.geometry}
                material={nodes.polySurface3043.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0202"
              position={[0.03, 0.02, -0.04]}
              rotation={[1.98, -0.68, -1.17]}
            >
              <group name="polySurface1044" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh091"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh091.geometry}
                  material={nodes.Mesh091.material}
                />
                <mesh
                  name="Mesh091_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh091_1.geometry}
                  material={nodes.Mesh091_1.material}
                />
              </group>
              <mesh
                name="polySurface3044"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3044.geometry}
                material={nodes.polySurface3044.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0203"
              position={[0, 0.04, -0.05]}
              rotation={[1.88, -0.47, -1.22]}
            >
              <group name="polySurface1045" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh093"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh093.geometry}
                  material={nodes.Mesh093.material}
                />
                <mesh
                  name="Mesh093_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh093_1.geometry}
                  material={nodes.Mesh093_1.material}
                />
              </group>
              <mesh
                name="polySurface3045"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3045.geometry}
                material={nodes.polySurface3045.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0204"
              position={[0.07, 0.08, 0.04]}
              rotation={[0.62, -0.45, -3]}
            >
              <group name="polySurface1054" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh111"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh111.geometry}
                  material={nodes.Mesh111.material}
                />
                <mesh
                  name="Mesh111_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh111_1.geometry}
                  material={nodes.Mesh111_1.material}
                />
              </group>
              <mesh
                name="polySurface3054"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3054.geometry}
                material={nodes.polySurface3054.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0205"
              position={[0.03, 0.08, 0.09]}
              rotation={[0.92, -0.46, -2.81]}
            >
              <group name="polySurface1055" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh113"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh113.geometry}
                  material={nodes.Mesh113.material}
                />
                <mesh
                  name="Mesh113_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh113_1.geometry}
                  material={nodes.Mesh113_1.material}
                />
              </group>
              <mesh
                name="polySurface3055"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3055.geometry}
                material={nodes.polySurface3055.material}
                position={[0, 0, -0.23]}
                scale={0.92}
              />
            </group>
            <group
              name="Dandelion_seed0206"
              position={[0.09, 0.05, 0.04]}
              rotation={[0.39, -0.65, -2.94]}
            >
              <group name="polySurface1056" scale={[0.8, 0.8, 1]}>
                <mesh
                  name="Mesh115"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh115.geometry}
                  material={nodes.Mesh115.material}
                />
                <mesh
                  name="Mesh115_1"
                  castShadow
                  receiveShadow
                  geometry={nodes.Mesh115_1.geometry}
                  material={nodes.Mesh115_1.material}
                />
              </group>
              <mesh
                name="polySurface3056"
                castShadow
                receiveShadow
                geometry={nodes.polySurface3056.geometry}
                material={nodes.polySurface3056.material}
                position={[0, 0, 0.97]}
                scale={[1.08, 1.08, 1.33]}
              />
            </group>
          </group>
          <group name="hold_seed">
            <group name="seed_A">
              <group name="Dandelion_seed01">
                <group name="polySurface1057" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh117"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh117.geometry}
                    material={nodes.Mesh117.material}
                  />
                  <mesh
                    name="Mesh117_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh117_1.geometry}
                    material={nodes.Mesh117_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3057"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3057.geometry}
                  material={nodes.polySurface3057.material}
                  position={[0, 0, 2.61]}
                  scale={[1.08, 1.08, 1.9]}
                />
              </group>
              <group
                name="Dandelion_seed0133"
                position={[-0.04, 0, 0]}
                rotation={[0, 0.23, 0]}
                scale={[1, 1, 1.02]}
              >
                <group name="polySurface1058" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh119"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh119.geometry}
                    material={nodes.Mesh119.material}
                  />
                  <mesh
                    name="Mesh119_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh119_1.geometry}
                    material={nodes.Mesh119_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3058"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3058.geometry}
                  material={nodes.polySurface3058.material}
                  position={[0, 0, 1.83]}
                  rotation={[0, 0, 0.9]}
                  scale={[1.08, 1.08, 1.63]}
                />
              </group>
              <group
                name="Dandelion_seed0134"
                position={[-0.07, 0, 0.01]}
                rotation={[0, 0.46, 0]}
                scale={[1, 1, 1.02]}
              >
                <group name="polySurface1059" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh121"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh121.geometry}
                    material={nodes.Mesh121.material}
                  />
                  <mesh
                    name="Mesh121_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh121_1.geometry}
                    material={nodes.Mesh121_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3059"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3059.geometry}
                  material={nodes.polySurface3059.material}
                  position={[0, 0, 1.14]}
                  rotation={[0, 0, 2.53]}
                  scale={[1.08, 1.08, 1.39]}
                />
              </group>
              <group
                name="Dandelion_seed0135001"
                position={[-0.1, 0, 0.04]}
                rotation={[0, 0.68, 0]}
                scale={[1, 1, 0.98]}
              >
                <group name="polySurface1060" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh123"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh123.geometry}
                    material={nodes.Mesh123.material}
                  />
                  <mesh
                    name="Mesh123_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh123_1.geometry}
                    material={nodes.Mesh123_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3060"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3060.geometry}
                  material={nodes.polySurface3060.material}
                  position={[0, 0, 2.92]}
                  rotation={[0, 0, -0.35]}
                  scale={[1.08, 1.08, 2.01]}
                />
              </group>
              <group
                name="Dandelion_seed0136001"
                position={[-0.11, 0, 0.05]}
                rotation={[0, 0.91, 0]}
                scale={[1, 1, 0.96]}
              >
                <group name="polySurface1061" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh125"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh125.geometry}
                    material={nodes.Mesh125.material}
                  />
                  <mesh
                    name="Mesh125_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh125_1.geometry}
                    material={nodes.Mesh125_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3061"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3061.geometry}
                  material={nodes.polySurface3061.material}
                  position={[0, 0, 3.68]}
                  rotation={[0, 0, 0.71]}
                  scale={[1.08, 1.08, 2.26]}
                />
              </group>
              <group
                name="Dandelion_seed0137001"
                position={[-0.11, 0, 0.05]}
                rotation={[0, 1.14, 0]}
                scale={[1, 1, 1.01]}
              >
                <group name="polySurface1062" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh127"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh127.geometry}
                    material={nodes.Mesh127.material}
                  />
                  <mesh
                    name="Mesh127_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh127_1.geometry}
                    material={nodes.Mesh127_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3062"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3062.geometry}
                  material={nodes.polySurface3062.material}
                  position={[0, 0, 1.17]}
                  rotation={[0, 0, 0.13]}
                  scale={[1.08, 1.08, 1.4]}
                />
              </group>
              <group
                name="Dandelion_seed0138001"
                position={[-0.1, 0, 0.05]}
                rotation={[0, 1.37, 0]}
                scale={[1, 1, 0.93]}
              >
                <group name="polySurface1063" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh129"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh129.geometry}
                    material={nodes.Mesh129.material}
                  />
                  <mesh
                    name="Mesh129_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh129_1.geometry}
                    material={nodes.Mesh129_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3063"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3063.geometry}
                  material={nodes.polySurface3063.material}
                  position={[0, 0, 0.24]}
                  rotation={[0, 0, 0.85]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0139001"
                position={[-0.11, 0, 0.04]}
                rotation={[-Math.PI, 1.55, -Math.PI]}
              >
                <group name="polySurface1064" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh131"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh131.geometry}
                    material={nodes.Mesh131.material}
                  />
                  <mesh
                    name="Mesh131_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh131_1.geometry}
                    material={nodes.Mesh131_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3064"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3064.geometry}
                  material={nodes.polySurface3064.material}
                  position={[0, 0, 0.24]}
                  rotation={[0, 0, -0.46]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0140001"
                position={[-0.08, 0, 0.01]}
                rotation={[-Math.PI, 1.32, -Math.PI]}
                scale={[1, 1, 0.94]}
              >
                <group name="polySurface1065" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh133"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh133.geometry}
                    material={nodes.Mesh133.material}
                  />
                  <mesh
                    name="Mesh133_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh133_1.geometry}
                    material={nodes.Mesh133_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3065"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3065.geometry}
                  material={nodes.polySurface3065.material}
                  position={[0, 0, 0.24]}
                  rotation={[0, 0, 0.37]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0141001"
                position={[-0.06, 0, -0.05]}
                rotation={[-Math.PI, 1.06, -Math.PI]}
                scale={[1, 1, 1.02]}
              >
                <group name="polySurface1066" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh135"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh135.geometry}
                    material={nodes.Mesh135.material}
                  />
                  <mesh
                    name="Mesh135_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh135_1.geometry}
                    material={nodes.Mesh135_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3066"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3066.geometry}
                  material={nodes.polySurface3066.material}
                  position={[0, 0, 0.24]}
                  rotation={[0, 0, 0.48]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0142"
                position={[0, -0.03, 0]}
                rotation={[-0.2, 0, 0]}
              >
                <group name="polySurface1067" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh137"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh137.geometry}
                    material={nodes.Mesh137.material}
                  />
                  <mesh
                    name="Mesh137_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh137_1.geometry}
                    material={nodes.Mesh137_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3067"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3067.geometry}
                  material={nodes.polySurface3067.material}
                  position={[0, 0, 1.82]}
                  scale={[1.08, 1.08, 1.62]}
                />
              </group>
              <group
                name="Dandelion_seed0143"
                position={[0, -0.06, 0.01]}
                rotation={[-0.39, 0, 0]}
              >
                <group name="polySurface1068" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh139"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh139.geometry}
                    material={nodes.Mesh139.material}
                  />
                  <mesh
                    name="Mesh139_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh139_1.geometry}
                    material={nodes.Mesh139_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3068"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3068.geometry}
                  material={nodes.polySurface3068.material}
                  position={[0, 0, 0.58]}
                  scale={[1.08, 1.08, 1.2]}
                />
              </group>
              <group
                name="Dandelion_seed0144"
                position={[0, -0.08, 0.02]}
                rotation={[-0.56, 0, 0]}
              >
                <group name="polySurface1069" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh141"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh141.geometry}
                    material={nodes.Mesh141.material}
                  />
                  <mesh
                    name="Mesh141_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh141_1.geometry}
                    material={nodes.Mesh141_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3069"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3069.geometry}
                  material={nodes.polySurface3069.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0145"
                position={[0, -0.11, 0.04]}
                rotation={[-0.77, 0, 0]}
              >
                <group name="polySurface1070" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh143"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh143.geometry}
                    material={nodes.Mesh143.material}
                  />
                  <mesh
                    name="Mesh143_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh143_1.geometry}
                    material={nodes.Mesh143_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3070"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3070.geometry}
                  material={nodes.polySurface3070.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0146"
                position={[0, -0.11, 0.08]}
                rotation={[-0.99, 0, 0]}
              >
                <group name="polySurface1071" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh145"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh145.geometry}
                    material={nodes.Mesh145.material}
                  />
                  <mesh
                    name="Mesh145_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh145_1.geometry}
                    material={nodes.Mesh145_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3071"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3071.geometry}
                  material={nodes.polySurface3071.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0147"
                position={[0, -0.11, 0.11]}
                rotation={[-1.2, 0, 0]}
              >
                <group name="polySurface1072" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh147"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh147.geometry}
                    material={nodes.Mesh147.material}
                  />
                  <mesh
                    name="Mesh147_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh147_1.geometry}
                    material={nodes.Mesh147_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3072"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3072.geometry}
                  material={nodes.polySurface3072.material}
                  position={[0, 0, 1.08]}
                  scale={[1.08, 1.08, 1.37]}
                />
              </group>
              <group
                name="Dandelion_seed0148"
                position={[0, -0.04, 0.15]}
                rotation={[-1.51, 0, 0]}
              >
                <group name="polySurface1073" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh149"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh149.geometry}
                    material={nodes.Mesh149.material}
                  />
                  <mesh
                    name="Mesh149_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh149_1.geometry}
                    material={nodes.Mesh149_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3073"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3073.geometry}
                  material={nodes.polySurface3073.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0149"
                position={[0, 0.02, 0.08]}
                rotation={[-1.77, 0, 0]}
              >
                <group name="polySurface1074" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh151"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh151.geometry}
                    material={nodes.Mesh151.material}
                  />
                  <mesh
                    name="Mesh151_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh151_1.geometry}
                    material={nodes.Mesh151_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3074"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3074.geometry}
                  material={nodes.polySurface3074.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0150"
                position={[-0.04, -0.05, 0.01]}
                rotation={[-0.32, 0.24, 0.03]}
              >
                <group name="polySurface1075" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh153"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh153.geometry}
                    material={nodes.Mesh153.material}
                  />
                  <mesh
                    name="Mesh153_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh153_1.geometry}
                    material={nodes.Mesh153_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3075"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3075.geometry}
                  material={nodes.polySurface3075.material}
                  position={[0, 0, 2.61]}
                  scale={[1.08, 1.08, 1.9]}
                />
              </group>
              <group
                name="Dandelion_seed0151"
                position={[-0.07, -0.05, 0.02]}
                rotation={[-0.38, 0.44, 0.12]}
              >
                <group name="polySurface1076" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh155"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh155.geometry}
                    material={nodes.Mesh155.material}
                  />
                  <mesh
                    name="Mesh155_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh155_1.geometry}
                    material={nodes.Mesh155_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3076"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3076.geometry}
                  material={nodes.polySurface3076.material}
                  position={[0, 0, 2.08]}
                  scale={[1.08, 1.08, 1.72]}
                />
              </group>
              <group
                name="Dandelion_seed0152001"
                position={[-0.09, -0.05, 0.04]}
                rotation={[-0.39, 0.65, 0.2]}
              >
                <group name="polySurface1077" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh157"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh157.geometry}
                    material={nodes.Mesh157.material}
                  />
                  <mesh
                    name="Mesh157_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh157_1.geometry}
                    material={nodes.Mesh157_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3077"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3077.geometry}
                  material={nodes.polySurface3077.material}
                  position={[0, 0, 1.72]}
                  scale={[1.08, 1.08, 1.59]}
                />
              </group>
              <group
                name="Dandelion_seed0153001"
                position={[-0.12, -0.04, 0.06]}
                rotation={[-0.44, 0.85, 0.29]}
              >
                <group name="polySurface1078" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh159"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh159.geometry}
                    material={nodes.Mesh159.material}
                  />
                  <mesh
                    name="Mesh159_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh159_1.geometry}
                    material={nodes.Mesh159_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3078"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3078.geometry}
                  material={nodes.polySurface3078.material}
                  position={[0, 0, 2.72]}
                  scale={[1.08, 1.08, 1.94]}
                />
              </group>
              <group
                name="Dandelion_seed0154001"
                position={[-0.1, -0.05, 0.1]}
                rotation={[-0.7, 0.98, 0.56]}
              >
                <group name="polySurface1079" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh161"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh161.geometry}
                    material={nodes.Mesh161.material}
                  />
                  <mesh
                    name="Mesh161_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh161_1.geometry}
                    material={nodes.Mesh161_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3079"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3079.geometry}
                  material={nodes.polySurface3079.material}
                  position={[0, 0, 0.98]}
                  scale={[1.08, 1.08, 1.34]}
                />
              </group>
              <group
                name="Dandelion_seed0155001"
                position={[-0.09, -0.02, 0.13]}
                rotation={[-0.8, 1.2, 0.72]}
              >
                <group name="polySurface1080" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh163"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh163.geometry}
                    material={nodes.Mesh163.material}
                  />
                  <mesh
                    name="Mesh163_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh163_1.geometry}
                    material={nodes.Mesh163_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3080"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3080.geometry}
                  material={nodes.polySurface3080.material}
                  position={[0, 0, 2.02]}
                  scale={[1.08, 1.08, 1.69]}
                />
              </group>
              <group
                name="Dandelion_seed0156001"
                position={[-0.05, -0.01, 0.15]}
                rotation={[-1.34, 1.27, 1.29]}
              >
                <group name="polySurface1081" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh165"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh165.geometry}
                    material={nodes.Mesh165.material}
                  />
                  <mesh
                    name="Mesh165_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh165_1.geometry}
                    material={nodes.Mesh165_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3081"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3081.geometry}
                  material={nodes.polySurface3081.material}
                  position={[0, 0, 1.01]}
                  scale={[1.08, 1.08, 1.35]}
                />
              </group>
              <group
                name="Dandelion_seed0157001"
                position={[-0.04, 0, -0.04]}
                rotation={[-2.41, 1.09, 2.43]}
              >
                <group name="polySurface1082" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh167"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh167.geometry}
                    material={nodes.Mesh167.material}
                  />
                  <mesh
                    name="Mesh167_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh167_1.geometry}
                    material={nodes.Mesh167_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3082"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3082.geometry}
                  material={nodes.polySurface3082.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0158"
                position={[-0.03, -0.08, 0.03]}
                rotation={[-0.57, 0.22, -0.03]}
              >
                <group name="polySurface1083" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh169"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh169.geometry}
                    material={nodes.Mesh169.material}
                  />
                  <mesh
                    name="Mesh169_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh169_1.geometry}
                    material={nodes.Mesh169_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3083"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3083.geometry}
                  material={nodes.polySurface3083.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0159001"
                position={[-0.07, -0.08, 0.04]}
                rotation={[-0.62, 0.45, 0.14]}
              >
                <group name="polySurface1084" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh171"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh171.geometry}
                    material={nodes.Mesh171.material}
                  />
                  <mesh
                    name="Mesh171_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh171_1.geometry}
                    material={nodes.Mesh171_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3084"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3084.geometry}
                  material={nodes.polySurface3084.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0160001"
                position={[-0.1, -0.08, 0.06]}
                rotation={[-0.73, 0.66, 0.34]}
              >
                <group name="polySurface1085" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh173"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh173.geometry}
                    material={nodes.Mesh173.material}
                  />
                  <mesh
                    name="Mesh173_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh173_1.geometry}
                    material={nodes.Mesh173_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3085"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3085.geometry}
                  material={nodes.polySurface3085.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0161001"
                position={[-0.08, -0.05, 0.12]}
                rotation={[-0.97, 0.89, 0.68]}
              >
                <group name="polySurface1086" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh175"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh175.geometry}
                    material={nodes.Mesh175.material}
                  />
                  <mesh
                    name="Mesh175_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh175_1.geometry}
                    material={nodes.Mesh175_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3086"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3086.geometry}
                  material={nodes.polySurface3086.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0162001"
                position={[-0.06, -0.04, 0.12]}
                rotation={[-1.39, 1.01, 1.2]}
              >
                <group name="polySurface1087" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh177"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh177.geometry}
                    material={nodes.Mesh177.material}
                  />
                  <mesh
                    name="Mesh177_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh177_1.geometry}
                    material={nodes.Mesh177_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3087"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3087.geometry}
                  material={nodes.polySurface3087.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0163001"
                position={[-0.03, -0.02, 0.07]}
                rotation={[-1.85, 1, 1.74]}
              >
                <group name="polySurface1088" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh179"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh179.geometry}
                    material={nodes.Mesh179.material}
                  />
                  <mesh
                    name="Mesh179_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh179_1.geometry}
                    material={nodes.Mesh179_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3088"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3088.geometry}
                  material={nodes.polySurface3088.material}
                  position={[0, 0, 0.48]}
                  scale={[0.92, 0.92, 1.17]}
                />
              </group>
              <group
                name="Dandelion_seed0164001"
                position={[-0.04, 0.01, -0.02]}
                rotation={[-2.29, 0.81, 2.28]}
              >
                <group name="polySurface1089" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh181"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh181.geometry}
                    material={nodes.Mesh181.material}
                  />
                  <mesh
                    name="Mesh181_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh181_1.geometry}
                    material={nodes.Mesh181_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3089"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3089.geometry}
                  material={nodes.polySurface3089.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0165001"
                position={[-0.03, -0.02, -0.04]}
                rotation={[-1.98, 0.68, 1.98]}
              >
                <group name="polySurface1090" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh183"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh183.geometry}
                    material={nodes.Mesh183.material}
                  />
                  <mesh
                    name="Mesh183_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh183_1.geometry}
                    material={nodes.Mesh183_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3090"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3090.geometry}
                  material={nodes.polySurface3090.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0166001"
                position={[-0.01, -0.05, 0.04]}
                rotation={[-1.51, 0.78, 1.28]}
              >
                <group name="polySurface1091" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh185"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh185.geometry}
                    material={nodes.Mesh185.material}
                  />
                  <mesh
                    name="Mesh185_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh185_1.geometry}
                    material={nodes.Mesh185_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3091"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3091.geometry}
                  material={nodes.polySurface3091.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0167001"
                position={[-0.02, -0.07, 0.08]}
                rotation={[-1.16, 0.68, 0.76]}
              >
                <group name="polySurface1092" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh187"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh187.geometry}
                    material={nodes.Mesh187.material}
                  />
                  <mesh
                    name="Mesh187_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh187_1.geometry}
                    material={nodes.Mesh187_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3092"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3092.geometry}
                  material={nodes.polySurface3092.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0168001"
                position={[-0.03, -0.08, 0.09]}
                rotation={[-0.92, 0.46, 0.33]}
              >
                <group name="polySurface1093" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh189"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh189.geometry}
                    material={nodes.Mesh189.material}
                  />
                  <mesh
                    name="Mesh189_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh189_1.geometry}
                    material={nodes.Mesh189_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3093"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3093.geometry}
                  material={nodes.polySurface3093.material}
                  position={[0, 0, 0.48]}
                  scale={[0.92, 0.92, 1.17]}
                />
                <mesh
                  name="polySurface35"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface35.geometry}
                  material={nodes.polySurface35.material}
                  position={[0.09, -0.32, 0.47]}
                  rotation={[0.01, -0.09, -0.01]}
                  scale={[0.92, 0.92, 1.17]}
                />
              </group>
              <group
                name="Dandelion_seed0169001"
                position={[-0.04, -0.11, 0.05]}
                rotation={[-0.83, 0.26, 0.07]}
              >
                <group name="polySurface1094" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh192"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh192.geometry}
                    material={nodes.Mesh192.material}
                  />
                  <mesh
                    name="Mesh192_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh192_1.geometry}
                    material={nodes.Mesh192_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3094"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3094.geometry}
                  material={nodes.polySurface3094.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
                <mesh
                  name="polySurface32"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface32.geometry}
                  material={nodes.polySurface32.material}
                  position={[0.16, 0.21, -0.24]}
                  rotation={[0.03, -0.09, 0.02]}
                  scale={[0.92, 0.92, 0.92]}
                />
              </group>
              <group
                name="Dandelion_seed0170001"
                position={[-0.05, 0.05, -0.04]}
                rotation={[-2.16, 0.55, 2.28]}
              >
                <group name="polySurface1095" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh195"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh195.geometry}
                    material={nodes.Mesh195.material}
                  />
                  <mesh
                    name="Mesh195_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh195_1.geometry}
                    material={nodes.Mesh195_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3095"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3095.geometry}
                  material={nodes.polySurface3095.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0171001"
                position={[-0.06, 0.01, -0.1]}
                rotation={[-2.12, 0.28, 2.25]}
              >
                <group name="polySurface1096" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh197"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh197.geometry}
                    material={nodes.Mesh197.material}
                  />
                  <mesh
                    name="Mesh197_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh197_1.geometry}
                    material={nodes.Mesh197_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3096"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3096.geometry}
                  material={nodes.polySurface3096.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0172001"
                position={[-0.04, -0.13, 0.08]}
                rotation={[-1.06, 0.24, 0.13]}
              >
                <group name="polySurface1097" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh199"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh199.geometry}
                    material={nodes.Mesh199.material}
                  />
                  <mesh
                    name="Mesh199_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh199_1.geometry}
                    material={nodes.Mesh199_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3097"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3097.geometry}
                  material={nodes.polySurface3097.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
                <mesh
                  name="polySurface34"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface34.geometry}
                  material={nodes.polySurface34.material}
                  position={[-0.2, 0.02, -0.19]}
                  rotation={[-0.07, 0.1, 0]}
                  scale={[0.92, 0.92, 0.92]}
                />
              </group>
              <group
                name="Dandelion_seed0173001"
                position={[-0.01, -0.12, 0.13]}
                rotation={[-1.3, 0.31, 0.48]}
              >
                <group name="polySurface1098" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh202"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh202.geometry}
                    material={nodes.Mesh202.material}
                  />
                  <mesh
                    name="Mesh202_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh202_1.geometry}
                    material={nodes.Mesh202_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3098"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3098.geometry}
                  material={nodes.polySurface3098.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
                <mesh
                  name="polySurface30"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface30.geometry}
                  material={nodes.polySurface30.material}
                  position={[0.28, 0.28, -0.24]}
                  rotation={[-0.14, 0, 0.1]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0174001"
                position={[0, -0.09, 0.1]}
                rotation={[-1.35, 0.51, 0.87]}
              >
                <group name="polySurface1099" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh205"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh205.geometry}
                    material={nodes.Mesh205.material}
                  />
                  <mesh
                    name="Mesh205_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh205_1.geometry}
                    material={nodes.Mesh205_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3099"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3099.geometry}
                  material={nodes.polySurface3099.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0175001"
                position={[0.02, -0.07, 0.04]}
                rotation={[-1.59, 0.55, 1.33]}
              >
                <group name="polySurface1100" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh207"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh207.geometry}
                    material={nodes.Mesh207.material}
                  />
                  <mesh
                    name="Mesh207_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh207_1.geometry}
                    material={nodes.Mesh207_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3100"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3100.geometry}
                  material={nodes.polySurface3100.material}
                  position={[0, 0, 0.48]}
                  scale={[0.92, 0.92, 1.17]}
                />
              </group>
              <group
                name="Dandelion_seed0176001"
                position={[0, -0.04, -0.05]}
                rotation={[-1.88, 0.47, 1.92]}
              >
                <group name="polySurface1101" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh209"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh209.geometry}
                    material={nodes.Mesh209.material}
                  />
                  <mesh
                    name="Mesh209_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh209_1.geometry}
                    material={nodes.Mesh209_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3101"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3101.geometry}
                  material={nodes.polySurface3101.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0177001"
                position={[0.04, -0.04, -0.03]}
                rotation={[-2, 0.2, 1.99]}
              >
                <group name="polySurface1102" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh211"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh211.geometry}
                    material={nodes.Mesh211.material}
                  />
                  <mesh
                    name="Mesh211_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh211_1.geometry}
                    material={nodes.Mesh211_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3102"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3102.geometry}
                  material={nodes.polySurface3102.material}
                  position={[0, 0, 0.48]}
                  scale={[0.92, 0.92, 1.17]}
                />
              </group>
              <group
                name="Dandelion_seed0178001"
                position={[0.03, -0.05, -0.06]}
                rotation={[-1.8, 0.26, 1.87]}
              >
                <group name="polySurface1103" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh213"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh213.geometry}
                    material={nodes.Mesh213.material}
                  />
                  <mesh
                    name="Mesh213_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh213_1.geometry}
                    material={nodes.Mesh213_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3103"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3103.geometry}
                  material={nodes.polySurface3103.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0179001"
                position={[0.03, -0.06, -0.08]}
                rotation={[-1.62, 0.27, 1.77]}
              >
                <group name="polySurface1104" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh215"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh215.geometry}
                    material={nodes.Mesh215.material}
                  />
                  <mesh
                    name="Mesh215_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh215_1.geometry}
                    material={nodes.Mesh215_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3104"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3104.geometry}
                  material={nodes.polySurface3104.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0180001"
                position={[-0.07, -0.06, -0.12]}
                rotation={[-Math.PI, 0.84, -Math.PI]}
              >
                <group name="polySurface1105" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh232"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh232.geometry}
                    material={nodes.Mesh232.material}
                  />
                  <mesh
                    name="Mesh232_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh232_1.geometry}
                    material={nodes.Mesh232_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3105"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3105.geometry}
                  material={nodes.polySurface3105.material}
                  position={[0, 0, 0.24]}
                  rotation={[0, 0, 0.48]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0181001"
                position={[-0.08, 0.02, -0.12]}
                rotation={[-2.6, 0.72, 2.49]}
              >
                <group name="polySurface1106" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh234"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh234.geometry}
                    material={nodes.Mesh234.material}
                  />
                  <mesh
                    name="Mesh234_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh234_1.geometry}
                    material={nodes.Mesh234_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3106"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3106.geometry}
                  material={nodes.polySurface3106.material}
                  position={[0, 0, 0.48]}
                  scale={[0.92, 0.92, 1.17]}
                />
              </group>
              <group
                name="Dandelion_seed0182001"
                position={[-0.08, 0.06, -0.11]}
                rotation={[-2.4, 0.45, 2.39]}
              >
                <group name="polySurface1107" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh236"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh236.geometry}
                    material={nodes.Mesh236.material}
                  />
                  <mesh
                    name="Mesh236_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh236_1.geometry}
                    material={nodes.Mesh236_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3107"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3107.geometry}
                  material={nodes.polySurface3107.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0183"
                position={[-0.09, 0.01, -0.14]}
                rotation={[-2.29, 0.14, 2.28]}
              >
                <group name="polySurface1108" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh238"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh238.geometry}
                    material={nodes.Mesh238.material}
                  />
                  <mesh
                    name="Mesh238_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh238_1.geometry}
                    material={nodes.Mesh238_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3108"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3108.geometry}
                  material={nodes.polySurface3108.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group name="fake">
                <group
                  name="polySurface10"
                  position={[0.06, 0.06, -0.05]}
                  rotation={[-0.93, 0.64, 0.59]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh223"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh223.geometry}
                    material={nodes.Mesh223.material}
                  />
                  <mesh
                    name="Mesh223_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh223_1.geometry}
                    material={nodes.Mesh223_1.material}
                  />
                </group>
                <group
                  name="polySurface11"
                  position={[0.06, 0.06, -0.04]}
                  rotation={[-0.8, 0.6, 0.56]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh224"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh224.geometry}
                    material={nodes.Mesh224.material}
                  />
                  <mesh
                    name="Mesh224_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh224_1.geometry}
                    material={nodes.Mesh224_1.material}
                  />
                </group>
                <group
                  name="polySurface12"
                  position={[0.04, 0.08, -0.06]}
                  rotation={[-1.12, 0.39, 0.97]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh225"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh225.geometry}
                    material={nodes.Mesh225.material}
                  />
                  <mesh
                    name="Mesh225_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh225_1.geometry}
                    material={nodes.Mesh225_1.material}
                  />
                </group>
                <group
                  name="polySurface13"
                  position={[0.06, 0.08, -0.1]}
                  rotation={[-1.51, 0.64, 0.91]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh226"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh226.geometry}
                    material={nodes.Mesh226.material}
                  />
                  <mesh
                    name="Mesh226_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh226_1.geometry}
                    material={nodes.Mesh226_1.material}
                  />
                </group>
                <group
                  name="polySurface14"
                  position={[0.07, 0.07, -0.09]}
                  rotation={[-1.44, 0.8, 0.89]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh227"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh227.geometry}
                    material={nodes.Mesh227.material}
                  />
                  <mesh
                    name="Mesh227_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh227_1.geometry}
                    material={nodes.Mesh227_1.material}
                  />
                </group>
                <group
                  name="polySurface15"
                  position={[0.07, 0.07, -0.1]}
                  rotation={[-1.58, 0.8, 0.91]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh228"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh228.geometry}
                    material={nodes.Mesh228.material}
                  />
                  <mesh
                    name="Mesh228_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh228_1.geometry}
                    material={nodes.Mesh228_1.material}
                  />
                </group>
                <group
                  name="polySurface16"
                  position={[0.07, 0.07, -0.12]}
                  rotation={[-1.78, 0.8, 0.93]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh229"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh229.geometry}
                    material={nodes.Mesh229.material}
                  />
                  <mesh
                    name="Mesh229_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh229_1.geometry}
                    material={nodes.Mesh229_1.material}
                  />
                </group>
                <group
                  name="polySurface17"
                  position={[0.05, 0.08, -0.14]}
                  rotation={[-2.02, 0.56, 0.89]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh230"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh230.geometry}
                    material={nodes.Mesh230.material}
                  />
                  <mesh
                    name="Mesh230_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh230_1.geometry}
                    material={nodes.Mesh230_1.material}
                  />
                </group>
                <group
                  name="polySurface18"
                  position={[0.06, 0.05, -0.04]}
                  rotation={[-0.67, 0.63, 0.01]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh231"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh231.geometry}
                    material={nodes.Mesh231.material}
                  />
                  <mesh
                    name="Mesh231_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh231_1.geometry}
                    material={nodes.Mesh231_1.material}
                  />
                </group>
                <group
                  name="polySurface4"
                  position={[0.02, 0.04, -0.01]}
                  rotation={[-0.36, 0.16, -0.03]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh217"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh217.geometry}
                    material={nodes.Mesh217.material}
                  />
                  <mesh
                    name="Mesh217_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh217_1.geometry}
                    material={nodes.Mesh217_1.material}
                  />
                </group>
                <group
                  name="polySurface5"
                  position={[0.04, 0.03, -0.01]}
                  rotation={[-0.35, 0.39, -0.03]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh218"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh218.geometry}
                    material={nodes.Mesh218.material}
                  />
                  <mesh
                    name="Mesh218_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh218_1.geometry}
                    material={nodes.Mesh218_1.material}
                  />
                </group>
                <group
                  name="polySurface6"
                  position={[0.06, 0.03, -0.02]}
                  rotation={[-0.35, 0.59, -0.03]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh219"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh219.geometry}
                    material={nodes.Mesh219.material}
                  />
                  <mesh
                    name="Mesh219_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh219_1.geometry}
                    material={nodes.Mesh219_1.material}
                  />
                </group>
                <group
                  name="polySurface7"
                  position={[0.07, 0.05, -0.05]}
                  rotation={[-0.8, 0.77, 0.4]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh220"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh220.geometry}
                    material={nodes.Mesh220.material}
                  />
                  <mesh
                    name="Mesh220_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh220_1.geometry}
                    material={nodes.Mesh220_1.material}
                  />
                </group>
                <group
                  name="polySurface8"
                  position={[0.09, 0.03, -0.06]}
                  rotation={[-0.69, 1.01, 0.24]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh221"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh221.geometry}
                    material={nodes.Mesh221.material}
                  />
                  <mesh
                    name="Mesh221_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh221_1.geometry}
                    material={nodes.Mesh221_1.material}
                  />
                </group>
                <group
                  name="polySurface9"
                  position={[0.07, 0.07, -0.08]}
                  rotation={[-1.27, 0.76, 0.61]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh222"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh222.geometry}
                    material={nodes.Mesh222.material}
                  />
                  <mesh
                    name="Mesh222_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh222_1.geometry}
                    material={nodes.Mesh222_1.material}
                  />
                </group>
              </group>
            </group>
            <group name="seed_B" rotation={[-Math.PI, 0, -Math.PI]} scale={-1}>
              <group
                name="Dandelion_seed0142001"
                position={[0, -0.03, 0]}
                rotation={[-0.2, 0, 0]}
              >
                <group name="polySurface1109" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh240"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh240.geometry}
                    material={nodes.Mesh240.material}
                  />
                  <mesh
                    name="Mesh240_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh240_1.geometry}
                    material={nodes.Mesh240_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3109"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3109.geometry}
                  material={nodes.polySurface3109.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0143001"
                position={[0, -0.06, 0.01]}
                rotation={[-0.39, 0, 0]}
              >
                <group name="polySurface1110" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh242"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh242.geometry}
                    material={nodes.Mesh242.material}
                  />
                  <mesh
                    name="Mesh242_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh242_1.geometry}
                    material={nodes.Mesh242_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3110"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3110.geometry}
                  material={nodes.polySurface3110.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0144001"
                position={[0, -0.08, 0.02]}
                rotation={[-0.56, 0, 0]}
              >
                <group name="polySurface1111" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh244"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh244.geometry}
                    material={nodes.Mesh244.material}
                  />
                  <mesh
                    name="Mesh244_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh244_1.geometry}
                    material={nodes.Mesh244_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3111"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3111.geometry}
                  material={nodes.polySurface3111.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0145001"
                position={[0, -0.11, 0.04]}
                rotation={[-0.77, 0, 0]}
              >
                <group name="polySurface1112" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh246"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh246.geometry}
                    material={nodes.Mesh246.material}
                  />
                  <mesh
                    name="Mesh246_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh246_1.geometry}
                    material={nodes.Mesh246_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3112"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3112.geometry}
                  material={nodes.polySurface3112.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0146001"
                position={[0, -0.11, 0.08]}
                rotation={[-0.99, 0, 0]}
              >
                <group name="polySurface1113" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh248"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh248.geometry}
                    material={nodes.Mesh248.material}
                  />
                  <mesh
                    name="Mesh248_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh248_1.geometry}
                    material={nodes.Mesh248_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3113"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3113.geometry}
                  material={nodes.polySurface3113.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0147001"
                position={[0, -0.11, 0.11]}
                rotation={[-1.2, 0, 0]}
              >
                <group name="polySurface1114" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh250"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh250.geometry}
                    material={nodes.Mesh250.material}
                  />
                  <mesh
                    name="Mesh250_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh250_1.geometry}
                    material={nodes.Mesh250_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3114"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3114.geometry}
                  material={nodes.polySurface3114.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0148001"
                position={[0, -0.04, 0.15]}
                rotation={[-1.51, 0, 0]}
              >
                <group name="polySurface1115" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh252"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh252.geometry}
                    material={nodes.Mesh252.material}
                  />
                  <mesh
                    name="Mesh252_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh252_1.geometry}
                    material={nodes.Mesh252_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3115"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3115.geometry}
                  material={nodes.polySurface3115.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0149001"
                position={[0, 0.02, 0.08]}
                rotation={[-1.77, 0, 0]}
              >
                <group name="polySurface1116" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh254"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh254.geometry}
                    material={nodes.Mesh254.material}
                  />
                  <mesh
                    name="Mesh254_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh254_1.geometry}
                    material={nodes.Mesh254_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3116"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3116.geometry}
                  material={nodes.polySurface3116.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0150001"
                position={[-0.04, -0.05, 0.01]}
                rotation={[-0.32, 0.24, 0.03]}
              >
                <group name="polySurface1117" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh256"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh256.geometry}
                    material={nodes.Mesh256.material}
                  />
                  <mesh
                    name="Mesh256_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh256_1.geometry}
                    material={nodes.Mesh256_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3117"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3117.geometry}
                  material={nodes.polySurface3117.material}
                  position={[0, 0, 1.98]}
                  scale={[1.08, 1.08, 1.68]}
                />
              </group>
              <group
                name="Dandelion_seed0151001"
                position={[-0.07, -0.05, 0.02]}
                rotation={[-0.38, 0.44, 0.12]}
              >
                <group name="polySurface1118" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh258"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh258.geometry}
                    material={nodes.Mesh258.material}
                  />
                  <mesh
                    name="Mesh258_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh258_1.geometry}
                    material={nodes.Mesh258_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3118"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3118.geometry}
                  material={nodes.polySurface3118.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0152002"
                position={[-0.09, -0.05, 0.04]}
                rotation={[-0.39, 0.65, 0.2]}
              >
                <group name="polySurface1119" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh260"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh260.geometry}
                    material={nodes.Mesh260.material}
                  />
                  <mesh
                    name="Mesh260_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh260_1.geometry}
                    material={nodes.Mesh260_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3119"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3119.geometry}
                  material={nodes.polySurface3119.material}
                  position={[0, 0, 0.97]}
                  scale={[1.08, 1.08, 1.33]}
                />
              </group>
              <group
                name="Dandelion_seed0153002"
                position={[-0.12, -0.04, 0.06]}
                rotation={[-0.44, 0.85, 0.29]}
              >
                <group name="polySurface1120" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh262"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh262.geometry}
                    material={nodes.Mesh262.material}
                  />
                  <mesh
                    name="Mesh262_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh262_1.geometry}
                    material={nodes.Mesh262_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3120"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3120.geometry}
                  material={nodes.polySurface3120.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0154002"
                position={[-0.1, -0.05, 0.1]}
                rotation={[-0.7, 0.98, 0.56]}
              >
                <group name="polySurface1121" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh264"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh264.geometry}
                    material={nodes.Mesh264.material}
                  />
                  <mesh
                    name="Mesh264_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh264_1.geometry}
                    material={nodes.Mesh264_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3121"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3121.geometry}
                  material={nodes.polySurface3121.material}
                  position={[0, 0, 1.12]}
                  scale={[1.08, 1.08, 1.39]}
                />
              </group>
              <group
                name="Dandelion_seed0155002"
                position={[-0.09, -0.02, 0.13]}
                rotation={[-0.8, 1.2, 0.72]}
              >
                <group name="polySurface1122" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh266"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh266.geometry}
                    material={nodes.Mesh266.material}
                  />
                  <mesh
                    name="Mesh266_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh266_1.geometry}
                    material={nodes.Mesh266_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3122"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3122.geometry}
                  material={nodes.polySurface3122.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0156002"
                position={[-0.05, -0.01, 0.15]}
                rotation={[-1.34, 1.27, 1.29]}
              >
                <group name="polySurface1123" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh268"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh268.geometry}
                    material={nodes.Mesh268.material}
                  />
                  <mesh
                    name="Mesh268_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh268_1.geometry}
                    material={nodes.Mesh268_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3123"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3123.geometry}
                  material={nodes.polySurface3123.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0157002"
                position={[-0.04, 0, -0.04]}
                rotation={[-2.41, 1.09, 2.43]}
              >
                <group name="polySurface1124" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh270"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh270.geometry}
                    material={nodes.Mesh270.material}
                  />
                  <mesh
                    name="Mesh270_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh270_1.geometry}
                    material={nodes.Mesh270_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3124"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3124.geometry}
                  material={nodes.polySurface3124.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0158001"
                position={[-0.03, -0.08, 0.03]}
                rotation={[-0.57, 0.22, -0.03]}
              >
                <group name="polySurface1125" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh272"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh272.geometry}
                    material={nodes.Mesh272.material}
                  />
                  <mesh
                    name="Mesh272_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh272_1.geometry}
                    material={nodes.Mesh272_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3125"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3125.geometry}
                  material={nodes.polySurface3125.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0159002"
                position={[-0.07, -0.08, 0.04]}
                rotation={[-0.62, 0.45, 0.14]}
              >
                <group name="polySurface1126" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh274"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh274.geometry}
                    material={nodes.Mesh274.material}
                  />
                  <mesh
                    name="Mesh274_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh274_1.geometry}
                    material={nodes.Mesh274_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3126"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3126.geometry}
                  material={nodes.polySurface3126.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0160002"
                position={[-0.1, -0.08, 0.06]}
                rotation={[-0.73, 0.66, 0.34]}
              >
                <group name="polySurface1127" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh276"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh276.geometry}
                    material={nodes.Mesh276.material}
                  />
                  <mesh
                    name="Mesh276_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh276_1.geometry}
                    material={nodes.Mesh276_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3127"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3127.geometry}
                  material={nodes.polySurface3127.material}
                  position={[0, 0, 0.42]}
                  scale={[0.92, 0.92, 1.14]}
                />
              </group>
              <group
                name="Dandelion_seed0161002"
                position={[-0.08, -0.05, 0.12]}
                rotation={[-0.97, 0.89, 0.68]}
              >
                <group name="polySurface1128" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh278"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh278.geometry}
                    material={nodes.Mesh278.material}
                  />
                  <mesh
                    name="Mesh278_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh278_1.geometry}
                    material={nodes.Mesh278_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3128"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3128.geometry}
                  material={nodes.polySurface3128.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0162002"
                position={[-0.06, -0.04, 0.12]}
                rotation={[-1.39, 1.01, 1.2]}
              >
                <group name="polySurface1129" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh280"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh280.geometry}
                    material={nodes.Mesh280.material}
                  />
                  <mesh
                    name="Mesh280_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh280_1.geometry}
                    material={nodes.Mesh280_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3129"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3129.geometry}
                  material={nodes.polySurface3129.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0163002"
                position={[-0.03, -0.02, 0.07]}
                rotation={[-1.85, 1, 1.74]}
              >
                <group name="polySurface1130" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh282"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh282.geometry}
                    material={nodes.Mesh282.material}
                  />
                  <mesh
                    name="Mesh282_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh282_1.geometry}
                    material={nodes.Mesh282_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3130"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3130.geometry}
                  material={nodes.polySurface3130.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0164002"
                position={[-0.04, 0.01, -0.02]}
                rotation={[-2.29, 0.81, 2.28]}
              >
                <group name="polySurface1131" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh284"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh284.geometry}
                    material={nodes.Mesh284.material}
                  />
                  <mesh
                    name="Mesh284_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh284_1.geometry}
                    material={nodes.Mesh284_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3131"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3131.geometry}
                  material={nodes.polySurface3131.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0165002"
                position={[-0.03, -0.02, -0.04]}
                rotation={[-1.98, 0.68, 1.98]}
              >
                <group name="polySurface1132" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh286"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh286.geometry}
                    material={nodes.Mesh286.material}
                  />
                  <mesh
                    name="Mesh286_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh286_1.geometry}
                    material={nodes.Mesh286_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3132"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3132.geometry}
                  material={nodes.polySurface3132.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0166002"
                position={[-0.01, -0.05, 0.04]}
                rotation={[-1.51, 0.78, 1.28]}
              >
                <group name="polySurface1133" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh288"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh288.geometry}
                    material={nodes.Mesh288.material}
                  />
                  <mesh
                    name="Mesh288_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh288_1.geometry}
                    material={nodes.Mesh288_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3133"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3133.geometry}
                  material={nodes.polySurface3133.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0167002"
                position={[-0.02, -0.07, 0.08]}
                rotation={[-1.16, 0.68, 0.76]}
              >
                <group name="polySurface1134" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh290"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh290.geometry}
                    material={nodes.Mesh290.material}
                  />
                  <mesh
                    name="Mesh290_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh290_1.geometry}
                    material={nodes.Mesh290_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3134"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3134.geometry}
                  material={nodes.polySurface3134.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0168002"
                position={[-0.03, -0.08, 0.09]}
                rotation={[-0.92, 0.46, 0.33]}
              >
                <group name="polySurface1135" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh292"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh292.geometry}
                    material={nodes.Mesh292.material}
                  />
                  <mesh
                    name="Mesh292_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh292_1.geometry}
                    material={nodes.Mesh292_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3135"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3135.geometry}
                  material={nodes.polySurface3135.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0169002"
                position={[-0.04, -0.11, 0.05]}
                rotation={[-0.83, 0.26, 0.07]}
              >
                <group name="polySurface1136" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh294"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh294.geometry}
                    material={nodes.Mesh294.material}
                  />
                  <mesh
                    name="Mesh294_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh294_1.geometry}
                    material={nodes.Mesh294_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3136"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3136.geometry}
                  material={nodes.polySurface3136.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0170002"
                position={[-0.05, 0.05, -0.04]}
                rotation={[-2.16, 0.55, 2.28]}
              >
                <group name="polySurface1137" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh296"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh296.geometry}
                    material={nodes.Mesh296.material}
                  />
                  <mesh
                    name="Mesh296_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh296_1.geometry}
                    material={nodes.Mesh296_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3137"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3137.geometry}
                  material={nodes.polySurface3137.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0171002"
                position={[0.01, -0.02, -0.11]}
                rotation={[-2.12, 0.28, 2.25]}
              >
                <group name="polySurface1138" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh298"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh298.geometry}
                    material={nodes.Mesh298.material}
                  />
                  <mesh
                    name="Mesh298_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh298_1.geometry}
                    material={nodes.Mesh298_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3138"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3138.geometry}
                  material={nodes.polySurface3138.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0172002"
                position={[-0.04, -0.13, 0.08]}
                rotation={[-1.06, 0.24, 0.13]}
              >
                <group name="polySurface1139" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh300"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh300.geometry}
                    material={nodes.Mesh300.material}
                  />
                  <mesh
                    name="Mesh300_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh300_1.geometry}
                    material={nodes.Mesh300_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3139"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3139.geometry}
                  material={nodes.polySurface3139.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0173002"
                position={[-0.01, -0.12, 0.13]}
                rotation={[-1.3, 0.31, 0.48]}
              >
                <group name="polySurface1140" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh302"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh302.geometry}
                    material={nodes.Mesh302.material}
                  />
                  <mesh
                    name="Mesh302_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh302_1.geometry}
                    material={nodes.Mesh302_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3140"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3140.geometry}
                  material={nodes.polySurface3140.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0174002"
                position={[0, -0.09, 0.1]}
                rotation={[-1.35, 0.51, 0.87]}
              >
                <group name="polySurface1141" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh304"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh304.geometry}
                    material={nodes.Mesh304.material}
                  />
                  <mesh
                    name="Mesh304_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh304_1.geometry}
                    material={nodes.Mesh304_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3141"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3141.geometry}
                  material={nodes.polySurface3141.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0175002"
                position={[0.02, -0.07, 0.04]}
                rotation={[-1.59, 0.55, 1.33]}
              >
                <group name="polySurface1142" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh306"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh306.geometry}
                    material={nodes.Mesh306.material}
                  />
                  <mesh
                    name="Mesh306_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh306_1.geometry}
                    material={nodes.Mesh306_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3142"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3142.geometry}
                  material={nodes.polySurface3142.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0176002"
                position={[0, -0.04, -0.05]}
                rotation={[-1.88, 0.47, 1.92]}
              >
                <group name="polySurface1143" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh308"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh308.geometry}
                    material={nodes.Mesh308.material}
                  />
                  <mesh
                    name="Mesh308_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh308_1.geometry}
                    material={nodes.Mesh308_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3143"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3143.geometry}
                  material={nodes.polySurface3143.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0177002"
                position={[0.04, -0.04, -0.03]}
                rotation={[-2, 0.2, 1.99]}
              >
                <group name="polySurface1144" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh310"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh310.geometry}
                    material={nodes.Mesh310.material}
                  />
                  <mesh
                    name="Mesh310_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh310_1.geometry}
                    material={nodes.Mesh310_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3144"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3144.geometry}
                  material={nodes.polySurface3144.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0178002"
                position={[0.03, -0.05, -0.06]}
                rotation={[-1.8, 0.26, 1.87]}
              >
                <group name="polySurface1145" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh312"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh312.geometry}
                    material={nodes.Mesh312.material}
                  />
                  <mesh
                    name="Mesh312_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh312_1.geometry}
                    material={nodes.Mesh312_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3145"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3145.geometry}
                  material={nodes.polySurface3145.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0179002"
                position={[0.03, -0.06, -0.08]}
                rotation={[-1.62, 0.27, 1.77]}
              >
                <group name="polySurface1146" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh314"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh314.geometry}
                    material={nodes.Mesh314.material}
                  />
                  <mesh
                    name="Mesh314_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh314_1.geometry}
                    material={nodes.Mesh314_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3146"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3146.geometry}
                  material={nodes.polySurface3146.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0184001"
                position={[-0.12, 0.01, -0.16]}
                rotation={[-2.64, 0.71, 2.54]}
              >
                <group name="polySurface1147" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh331"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh331.geometry}
                    material={nodes.Mesh331.material}
                  />
                  <mesh
                    name="Mesh331_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh331_1.geometry}
                    material={nodes.Mesh331_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3147"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3147.geometry}
                  material={nodes.polySurface3147.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0185"
                position={[-0.13, 0.04, -0.16]}
                rotation={[-2.4, 0.46, 2.39]}
              >
                <group name="polySurface1148" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh333"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh333.geometry}
                    material={nodes.Mesh333.material}
                  />
                  <mesh
                    name="Mesh333_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh333_1.geometry}
                    material={nodes.Mesh333_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3148"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3148.geometry}
                  material={nodes.polySurface3148.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0186"
                position={[-0.13, -0.03, -0.19]}
                rotation={[-2.38, 0.24, 2.3]}
              >
                <group name="polySurface1149" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh335"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh335.geometry}
                    material={nodes.Mesh335.material}
                  />
                  <mesh
                    name="Mesh335_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh335_1.geometry}
                    material={nodes.Mesh335_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3149"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3149.geometry}
                  material={nodes.polySurface3149.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0187"
                position={[0.03, -0.05, -0.09]}
                rotation={[-2.22, 0.01, 2.02]}
              >
                <group name="polySurface1150" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh337"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh337.geometry}
                    material={nodes.Mesh337.material}
                  />
                  <mesh
                    name="Mesh337_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh337_1.geometry}
                    material={nodes.Mesh337_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3150"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3150.geometry}
                  material={nodes.polySurface3150.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group name="fake001">
                <group
                  name="polySurface10001"
                  position={[0.06, 0.06, -0.05]}
                  rotation={[-0.93, 0.64, 0.59]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh322"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh322.geometry}
                    material={nodes.Mesh322.material}
                  />
                  <mesh
                    name="Mesh322_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh322_1.geometry}
                    material={nodes.Mesh322_1.material}
                  />
                </group>
                <group
                  name="polySurface11001"
                  position={[0.06, 0.06, -0.04]}
                  rotation={[-0.8, 0.6, 0.56]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh323"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh323.geometry}
                    material={nodes.Mesh323.material}
                  />
                  <mesh
                    name="Mesh323_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh323_1.geometry}
                    material={nodes.Mesh323_1.material}
                  />
                </group>
                <group
                  name="polySurface12001"
                  position={[0.04, 0.08, -0.06]}
                  rotation={[-1.12, 0.39, 0.97]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh324"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh324.geometry}
                    material={nodes.Mesh324.material}
                  />
                  <mesh
                    name="Mesh324_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh324_1.geometry}
                    material={nodes.Mesh324_1.material}
                  />
                </group>
                <group
                  name="polySurface13001"
                  position={[0.06, 0.08, -0.1]}
                  rotation={[-1.51, 0.64, 0.91]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh325"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh325.geometry}
                    material={nodes.Mesh325.material}
                  />
                  <mesh
                    name="Mesh325_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh325_1.geometry}
                    material={nodes.Mesh325_1.material}
                  />
                </group>
                <group
                  name="polySurface14001"
                  position={[0.07, 0.07, -0.09]}
                  rotation={[-1.44, 0.8, 0.89]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh326"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh326.geometry}
                    material={nodes.Mesh326.material}
                  />
                  <mesh
                    name="Mesh326_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh326_1.geometry}
                    material={nodes.Mesh326_1.material}
                  />
                </group>
                <group
                  name="polySurface15001"
                  position={[0.07, 0.07, -0.1]}
                  rotation={[-1.58, 0.8, 0.91]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh327"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh327.geometry}
                    material={nodes.Mesh327.material}
                  />
                  <mesh
                    name="Mesh327_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh327_1.geometry}
                    material={nodes.Mesh327_1.material}
                  />
                </group>
                <group
                  name="polySurface16001"
                  position={[0.07, 0.07, -0.12]}
                  rotation={[-1.78, 0.8, 0.93]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh328"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh328.geometry}
                    material={nodes.Mesh328.material}
                  />
                  <mesh
                    name="Mesh328_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh328_1.geometry}
                    material={nodes.Mesh328_1.material}
                  />
                </group>
                <group
                  name="polySurface17001"
                  position={[0.05, 0.08, -0.14]}
                  rotation={[-2.02, 0.56, 0.89]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh329"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh329.geometry}
                    material={nodes.Mesh329.material}
                  />
                  <mesh
                    name="Mesh329_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh329_1.geometry}
                    material={nodes.Mesh329_1.material}
                  />
                </group>
                <group
                  name="polySurface18001"
                  position={[0.06, 0.05, -0.04]}
                  rotation={[-0.67, 0.63, 0.01]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh330"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh330.geometry}
                    material={nodes.Mesh330.material}
                  />
                  <mesh
                    name="Mesh330_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh330_1.geometry}
                    material={nodes.Mesh330_1.material}
                  />
                </group>
                <group
                  name="polySurface4001"
                  position={[0.02, 0.04, -0.01]}
                  rotation={[-0.36, 0.16, -0.03]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh316"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh316.geometry}
                    material={nodes.Mesh316.material}
                  />
                  <mesh
                    name="Mesh316_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh316_1.geometry}
                    material={nodes.Mesh316_1.material}
                  />
                </group>
                <group
                  name="polySurface5001"
                  position={[0.04, 0.03, -0.01]}
                  rotation={[-0.35, 0.39, -0.03]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh317"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh317.geometry}
                    material={nodes.Mesh317.material}
                  />
                  <mesh
                    name="Mesh317_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh317_1.geometry}
                    material={nodes.Mesh317_1.material}
                  />
                </group>
                <group
                  name="polySurface6001"
                  position={[0.06, 0.03, -0.02]}
                  rotation={[-0.35, 0.59, -0.03]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh318"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh318.geometry}
                    material={nodes.Mesh318.material}
                  />
                  <mesh
                    name="Mesh318_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh318_1.geometry}
                    material={nodes.Mesh318_1.material}
                  />
                </group>
                <group
                  name="polySurface7001"
                  position={[0.07, 0.05, -0.05]}
                  rotation={[-0.8, 0.77, 0.4]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh319"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh319.geometry}
                    material={nodes.Mesh319.material}
                  />
                  <mesh
                    name="Mesh319_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh319_1.geometry}
                    material={nodes.Mesh319_1.material}
                  />
                </group>
                <group
                  name="polySurface8001"
                  position={[0.09, 0.03, -0.06]}
                  rotation={[-0.69, 1.01, 0.24]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh320"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh320.geometry}
                    material={nodes.Mesh320.material}
                  />
                  <mesh
                    name="Mesh320_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh320_1.geometry}
                    material={nodes.Mesh320_1.material}
                  />
                </group>
                <group
                  name="polySurface9001"
                  position={[0.07, 0.07, -0.08]}
                  rotation={[-1.27, 0.76, 0.61]}
                  scale={[0.3, 0.3, 1]}
                >
                  <mesh
                    name="Mesh321"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh321.geometry}
                    material={nodes.Mesh321.material}
                  />
                  <mesh
                    name="Mesh321_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh321_1.geometry}
                    material={nodes.Mesh321_1.material}
                  />
                </group>
              </group>
            </group>
            <group name="seed_C" rotation={[-Math.PI, 0, 0]} scale={-1}>
              <group
                name="Dandelion_seed0133001"
                position={[-0.04, 0, 0]}
                rotation={[0, 0.23, 0]}
                scale={[1, 1, 1.02]}
              >
                <group name="polySurface1151" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh339"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh339.geometry}
                    material={nodes.Mesh339.material}
                  />
                  <mesh
                    name="Mesh339_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh339_1.geometry}
                    material={nodes.Mesh339_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3151"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3151.geometry}
                  material={nodes.polySurface3151.material}
                  position={[0, 0, 1.83]}
                  rotation={[0, 0, 0.9]}
                  scale={[1.08, 1.08, 1.63]}
                />
              </group>
              <group
                name="Dandelion_seed0134001"
                position={[-0.07, 0, 0.01]}
                rotation={[0, 0.46, 0]}
                scale={[1, 1, 1.02]}
              >
                <group name="polySurface1152" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh341"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh341.geometry}
                    material={nodes.Mesh341.material}
                  />
                  <mesh
                    name="Mesh341_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh341_1.geometry}
                    material={nodes.Mesh341_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3152"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3152.geometry}
                  material={nodes.polySurface3152.material}
                  position={[0, 0, 1.14]}
                  rotation={[0, 0, 0.22]}
                  scale={[1.08, 1.08, 1.39]}
                />
              </group>
              <group
                name="Dandelion_seed0150002"
                position={[-0.04, -0.05, 0.01]}
                rotation={[-0.32, 0.24, 0.03]}
              >
                <group name="polySurface1153" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh343"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh343.geometry}
                    material={nodes.Mesh343.material}
                  />
                  <mesh
                    name="Mesh343_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh343_1.geometry}
                    material={nodes.Mesh343_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3153"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3153.geometry}
                  material={nodes.polySurface3153.material}
                  position={[0, 0, 2.61]}
                  scale={[1.08, 1.08, 1.9]}
                />
              </group>
              <group
                name="Dandelion_seed0151002"
                position={[-0.07, -0.05, 0.02]}
                rotation={[-0.38, 0.44, 0.12]}
              >
                <group name="polySurface1154" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh345"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh345.geometry}
                    material={nodes.Mesh345.material}
                  />
                  <mesh
                    name="Mesh345_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh345_1.geometry}
                    material={nodes.Mesh345_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3154"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3154.geometry}
                  material={nodes.polySurface3154.material}
                  position={[0, 0, 2.08]}
                  scale={[1.08, 1.08, 1.72]}
                />
              </group>
              <group
                name="Dandelion_seed0158002"
                position={[-0.03, -0.08, 0.03]}
                rotation={[-0.57, 0.22, -0.03]}
              >
                <group name="polySurface1155" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh347"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh347.geometry}
                    material={nodes.Mesh347.material}
                  />
                  <mesh
                    name="Mesh347_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh347_1.geometry}
                    material={nodes.Mesh347_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3155"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3155.geometry}
                  material={nodes.polySurface3155.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0183001"
                position={[-0.09, 0.01, -0.14]}
                rotation={[-2.29, 0.14, 2.28]}
              >
                <group name="polySurface1156" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh349"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh349.geometry}
                    material={nodes.Mesh349.material}
                  />
                  <mesh
                    name="Mesh349_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh349_1.geometry}
                    material={nodes.Mesh349_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3156"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3156.geometry}
                  material={nodes.polySurface3156.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
            </group>
            <group name="seed_D" rotation={[0, 0, -Math.PI]}>
              <group
                name="Dandelion_seed0150003"
                position={[-0.04, -0.05, 0.01]}
                rotation={[-0.32, 0.24, 0.03]}
              >
                <group name="polySurface1157" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh351"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh351.geometry}
                    material={nodes.Mesh351.material}
                  />
                  <mesh
                    name="Mesh351_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh351_1.geometry}
                    material={nodes.Mesh351_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3157"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3157.geometry}
                  material={nodes.polySurface3157.material}
                  position={[0, 0, 1.98]}
                  scale={[1.08, 1.08, 1.68]}
                />
              </group>
              <group
                name="Dandelion_seed0151003"
                position={[-0.07, -0.05, 0.02]}
                rotation={[-0.38, 0.44, 0.12]}
              >
                <group name="polySurface1158" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh353"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh353.geometry}
                    material={nodes.Mesh353.material}
                  />
                  <mesh
                    name="Mesh353_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh353_1.geometry}
                    material={nodes.Mesh353_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3158"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3158.geometry}
                  material={nodes.polySurface3158.material}
                  position={[0, 0, 0.24]}
                  scale={1.08}
                />
              </group>
              <group
                name="Dandelion_seed0158003"
                position={[-0.03, -0.08, 0.03]}
                rotation={[-0.57, 0.22, -0.03]}
              >
                <group name="polySurface1159" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh355"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh355.geometry}
                    material={nodes.Mesh355.material}
                  />
                  <mesh
                    name="Mesh355_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh355_1.geometry}
                    material={nodes.Mesh355_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3159"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3159.geometry}
                  material={nodes.polySurface3159.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0169003"
                position={[-0.04, -0.11, 0.05]}
                rotation={[-0.83, 0.26, 0.07]}
              >
                <group name="polySurface1160" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh357"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh357.geometry}
                    material={nodes.Mesh357.material}
                  />
                  <mesh
                    name="Mesh357_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh357_1.geometry}
                    material={nodes.Mesh357_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3160"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3160.geometry}
                  material={nodes.polySurface3160.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0170003"
                position={[-0.05, 0.05, -0.04]}
                rotation={[-2.16, 0.55, 2.28]}
              >
                <group name="polySurface1161" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh359"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh359.geometry}
                    material={nodes.Mesh359.material}
                  />
                  <mesh
                    name="Mesh359_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh359_1.geometry}
                    material={nodes.Mesh359_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3161"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3161.geometry}
                  material={nodes.polySurface3161.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0171003"
                position={[0.01, -0.02, -0.11]}
                rotation={[-2.12, 0.28, 2.25]}
              >
                <group name="polySurface1162" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh361"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh361.geometry}
                    material={nodes.Mesh361.material}
                  />
                  <mesh
                    name="Mesh361_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh361_1.geometry}
                    material={nodes.Mesh361_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3162"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3162.geometry}
                  material={nodes.polySurface3162.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0172003"
                position={[-0.04, -0.13, 0.08]}
                rotation={[-1.06, 0.24, 0.13]}
              >
                <group name="polySurface1163" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh363"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh363.geometry}
                    material={nodes.Mesh363.material}
                  />
                  <mesh
                    name="Mesh363_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh363_1.geometry}
                    material={nodes.Mesh363_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3163"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3163.geometry}
                  material={nodes.polySurface3163.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0173003"
                position={[-0.01, -0.12, 0.13]}
                rotation={[-1.3, 0.31, 0.48]}
              >
                <group name="polySurface1164" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh365"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh365.geometry}
                    material={nodes.Mesh365.material}
                  />
                  <mesh
                    name="Mesh365_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh365_1.geometry}
                    material={nodes.Mesh365_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3164"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3164.geometry}
                  material={nodes.polySurface3164.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0177003"
                position={[0.04, -0.04, -0.03]}
                rotation={[-2, 0.2, 1.99]}
              >
                <group name="polySurface1165" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh367"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh367.geometry}
                    material={nodes.Mesh367.material}
                  />
                  <mesh
                    name="Mesh367_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh367_1.geometry}
                    material={nodes.Mesh367_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3165"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3165.geometry}
                  material={nodes.polySurface3165.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0178003"
                position={[0.03, -0.05, -0.06]}
                rotation={[-1.8, 0.26, 1.87]}
              >
                <group name="polySurface1166" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh369"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh369.geometry}
                    material={nodes.Mesh369.material}
                  />
                  <mesh
                    name="Mesh369_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh369_1.geometry}
                    material={nodes.Mesh369_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3166"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3166.geometry}
                  material={nodes.polySurface3166.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0179003"
                position={[0.03, -0.06, -0.08]}
                rotation={[-1.62, 0.27, 1.77]}
              >
                <group name="polySurface1167" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh371"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh371.geometry}
                    material={nodes.Mesh371.material}
                  />
                  <mesh
                    name="Mesh371_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh371_1.geometry}
                    material={nodes.Mesh371_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3167"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3167.geometry}
                  material={nodes.polySurface3167.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0185001"
                position={[-0.13, 0.04, -0.16]}
                rotation={[-2.4, 0.46, 2.39]}
              >
                <group name="polySurface1168" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh373"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh373.geometry}
                    material={nodes.Mesh373.material}
                  />
                  <mesh
                    name="Mesh373_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh373_1.geometry}
                    material={nodes.Mesh373_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3168"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3168.geometry}
                  material={nodes.polySurface3168.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
              <group
                name="Dandelion_seed0186001"
                position={[-0.13, -0.03, -0.19]}
                rotation={[-2.38, 0.24, 2.3]}
              >
                <group name="polySurface1169" scale={[0.8, 0.8, 1]}>
                  <mesh
                    name="Mesh375"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh375.geometry}
                    material={nodes.Mesh375.material}
                  />
                  <mesh
                    name="Mesh375_1"
                    castShadow
                    receiveShadow
                    geometry={nodes.Mesh375_1.geometry}
                    material={nodes.Mesh375_1.material}
                  />
                </group>
                <mesh
                  name="polySurface3169"
                  castShadow
                  receiveShadow
                  geometry={nodes.polySurface3169.geometry}
                  material={nodes.polySurface3169.material}
                  position={[0, 0, -0.23]}
                  scale={0.92}
                />
              </group>
            </group>
          </group>
          <mesh
            name="pCylinder1"
            castShadow
            receiveShadow
            geometry={nodes.pCylinder1.geometry}
            material={materials.VRayMtl4}
            position={[0, 0, 0.88]}
            scale={[0.22, 0.22, 0.68]}
          />
          <mesh
            name="polySurface2"
            castShadow
            receiveShadow
            geometry={nodes.polySurface2.geometry}
            material={materials.VRayMtl6}
            position={[0, 0, -0.03]}
            scale={[1.55, 1.55, 1]}
          />
          <mesh
            name="pSphere1"
            castShadow
            receiveShadow
            geometry={nodes.pSphere1.geometry}
            material={materials.VRayMtl5}
            position={[0, 0, 0.04]}
            scale={0.56}
          />
        </group>
        <group
          name="VRayLightDome1"
          rotation={[Math.PI / 2, 0, 0]}
          scale={0.01}
        />
      </group>
    </group>
  );
}
