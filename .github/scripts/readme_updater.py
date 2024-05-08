import re


def read_properties(file_path):
    properties = {}
    with open(file_path, 'r') as file:
        for line in file:
            if "=" in line:
                key, value = line.strip().split('=', 1)
                properties[key.strip()] = value.strip()
    return properties


def update_readme(readme_path, versions):
    with open(readme_path, 'r') as file:
        content = file.readlines()

    new_content = []
    for line in content:
        new_line = line
        for version_key, version_value in versions.items():
            # Match badge URLs and update the version
            new_line = re.sub(
                r'(https://img\.shields\.io/badge/' + re.escape(version_key) +
                r' Version-)\d+\.\d+\.\d+(-?[a-zA-Z0-9_.]*)',
                r'\g<1>' + version_value + r'\2', new_line
            )
        new_content.append(new_line)

    with open(readme_path, 'w') as file:
        file.writelines(new_content)


if __name__ == '__main__':
    properties = read_properties('gradle.properties')
    versions = {
        'Minecraft': properties.get('minecraftVersion', '1.19.2'),  # default if not in properties
        'Forge': properties.get('forgeVersion', '43.2.0'),
        'CCT': properties.get('cctVersion', '1.103.1'),
        'IE': properties.get('ieVersion', '9.0.0-153')
    }
    update_readme('README.md', versions)