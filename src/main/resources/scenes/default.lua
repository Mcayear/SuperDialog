openCount = nil;

function main()
    openCount = share:getIntValue("openCount", 0);
    openCount = openCount + 1;
    local playerName = player:getName()  -- 调用Java方法获取玩家名称
    local entityName = entity:getName().."§r"  -- 调用Java方法获取实体名称
    local title = "Welcome " .. playerName .. "!"
    local description = "You are interacting with " .. entityName .. ".\nopen count: ".. openCount ..".\nChoose again:"
    local options = {"Option 1", "Option 2"}
    return {title, description, options, { "oneDialog", "twoDialog" }}
end

function oneDialog()
    openCount = openCount + 1;
    local playerName = player:getName()
    local entityName = entity:getName().."§r"
    local title = "You chose Option 1, " .. playerName .. "!"
    local description = "You are still interacting with " .. entityName .. ".\nopen count: ".. openCount ..".\nChoose again:"
    return {title, description, options, { "close", "twoDialog" }}
end

function twoDialog()
    openCount = openCount + 1;
    local playerName = player:getName()
    local entityName = entity:getName().."§r"
    local title = "You chose Option 2, " .. playerName .. "!"
    local description = "You are still interacting with " .. entityName .. ".\nopen count: ".. openCount ..".\nChoose again:"
    local options = {"Option 1", "Close"}
    return {title, description, options, { "oneDialog", "close" }}
end

function close()
    -- 此函数没有返回值
    share:setIntValue("openCount", openCount);
end
